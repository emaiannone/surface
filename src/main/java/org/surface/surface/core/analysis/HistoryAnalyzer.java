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

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryAnalyzer extends Analyzer {
    private static final Logger LOGGER = LogManager.getLogger();

    private final String projectName;
    private final RevisionSelector revisionSelector;
    private final SetupEnvironmentAction setupEnvironmentAction;

    public HistoryAnalyzer(String projectName, String filesRegex, MetricsManager metricsManager, boolean includeTests, RevisionSelector revisionSelector, SetupEnvironmentAction setupEnvironmentAction) {
        super(filesRegex ,metricsManager, includeTests);
        this.projectName = projectName;
        this.revisionSelector = revisionSelector;
        this.setupEnvironmentAction = setupEnvironmentAction;
    }

    public AnalysisResults analyze() throws Exception {
        Path tmpDirPath = setupEnvironmentAction.setupEnvironment();
        Path projectDirPath = Paths.get(tmpDirPath.toString(), projectName);

        SigIntHandler sigIntHandler = new SigIntHandler(tmpDirPath);
        Runtime.getRuntime().addShutdownHook(sigIntHandler);

        AnalysisResults analysisResults = new AnalysisResults();
        List<String> notProcessedCommits = new ArrayList<>();
        try (Git git = Git.open(projectDirPath.toFile())) {
            List<RevCommit> commits;
            try {
                resetHard(git);
            } catch (GitAPIException e) {
                throw new RuntimeException("Failed to reset the state of git repository " + git.getRepository().getDirectory().getName(), e);
            }
            try {
                commits = revisionSelector.selectRevisions(git);
            } catch (Exception e) {
                throw new RuntimeException("Failed to fetch the required revisions from git repository " + projectDirPath, e);
            }
            Collections.reverse(commits);
            int numCommits = commits.size();
            LOGGER.info("* Analyzing {} commits in git repository {}", numCommits, projectDirPath);
            try (ProgressBar progressBar = new ProgressBarBuilder()
                    .setTaskName("Project " + projectName)
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
                    SnapshotAnalyzer snapshotAnalyzer = new SnapshotAnalyzer(projectDirPath, getFilesRegex(), getMetricsManager(), isIncludeTests());
                    AnalysisResults snapshotAnalyzerResults = snapshotAnalyzer.analyze();
                    analysisResults.addProjectResult(commit.getName(), snapshotAnalyzerResults.getFirstProjectResult());
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
        return analysisResults;
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
