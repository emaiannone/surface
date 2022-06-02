package org.surface.surface.core.analysis;

import me.tongfei.progressbar.ConsoleProgressBarConsumer;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.surface.surface.core.analysis.selectors.RevisionSelector;
import org.surface.surface.core.analysis.setup.SetupEnvironmentAction;
import org.surface.surface.core.metrics.api.MetricsManager;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class HistoryAnalyzer {
    private static final Logger LOGGER = LogManager.getLogger();

    private final String projectName;
    private final String filesRegex;
    private final MetricsManager metricsManager;
    private final RevisionSelector revisionSelector;
    private final SetupEnvironmentAction setupEnvironmentAction;

    public HistoryAnalyzer(String projectName, String filesRegex, MetricsManager metricsManager, RevisionSelector revisionSelector, SetupEnvironmentAction setupEnvironmentAction) {
        this.projectName = projectName;
        this.filesRegex = filesRegex;
        this.metricsManager = metricsManager;
        this.revisionSelector = revisionSelector;
        this.setupEnvironmentAction = setupEnvironmentAction;
    }

    public Map<String, ProjectMetricsResults> analyze() throws Exception {
        Path tmpDirPath = setupEnvironmentAction.setupEnvironment();
        Path projectDirPath = Paths.get(tmpDirPath.toString(), projectName);

        SigIntHandler sigIntHandler = new SigIntHandler(tmpDirPath);
        Runtime.getRuntime().addShutdownHook(sigIntHandler);

        Map<String, ProjectMetricsResults> allResults = new LinkedHashMap<>();
        List<String> notProcessedCommits = new ArrayList<>();
        try (Git git = Git.open(projectDirPath.toFile())) {
            List<RevCommit> commits;
            try {
                resetHard(git);
            } catch (GitAPIException e) {
                throw new RuntimeException("The reset of git repository " + git.getRepository().getDirectory().getName() + "failed", e);
            }
            try {
                commits = revisionSelector.selectRevisions(git);
            } catch (Exception e) {
                throw new Exception("Failed to fetch the required revisions from git repository " + projectDirPath, e);
            }
            Collections.reverse(commits);
            int numCommits = commits.size();
            LOGGER.info("* Going to analyze {} commits in git repository {}", numCommits, projectDirPath);
            try (ProgressBar progressBar = new ProgressBarBuilder()
                    .setTaskName("History Analysis")
                    .setInitialMax(numCommits)
                    .setStyle(ProgressBarStyle.ASCII)
                    .setMaxRenderedLength(150)
                    .setConsumer(new ConsoleProgressBarConsumer(System.out, 141))
                    //.setConsumer(new DelegatingProgressBarConsumer(LOGGER::info, 141))
                    //.setUpdateIntervalMillis(1)
                    .build()) {
                for (RevCommit commit : commits) {
                    try {
                        git.checkout().setName(commit.getName()).call();
                    } catch (GitAPIException e) {
                        notProcessedCommits.add(commit.getName());
                        continue;
                    }
                    progressBar.setExtraMessage("Inspecting " + commit.getName().substring(0, 8));
                    progressBar.step();
                    SnapshotAnalyzer snapshotAnalyzer = new SnapshotAnalyzer(projectDirPath, filesRegex, metricsManager);
                    ProjectMetricsResults projectMetricsResults = snapshotAnalyzer.analyze();
                    allResults.put(commit.getName(), projectMetricsResults);
                }
            }
        } catch (IOException e) {
            throw new IOException("Could not open git repository " + projectDirPath, e);
        } finally {
            if (notProcessedCommits.size() > 0) {
                LOGGER.warn("* Failed to process the following commits: " + notProcessedCommits);
            }
            FileUtils.deleteDirectory(tmpDirPath.toFile());
            Runtime.getRuntime().removeShutdownHook(sigIntHandler);
        }
        return allResults;
    }

    private void resetHard(Git git) throws GitAPIException {
        git.reset().setMode(ResetCommand.ResetType.HARD).call();
    }

    private static class SigIntHandler extends Thread {
        private final Path tmpDirPath;

        private SigIntHandler(Path tmpDirPath) {
            this.tmpDirPath = tmpDirPath;
        }

        @Override
        public void run() {
            try {
                FileUtils.deleteDirectory(tmpDirPath.toFile());
            } catch (IOException e) {
                LOGGER.warn("* Could not delete the working directory");
            }
        }
    }
}
