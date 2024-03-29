package org.surface.surface.core.engine.analysis;

import me.tongfei.progressbar.ConsoleProgressBarConsumer;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.surface.surface.core.engine.analysis.results.HistoryAnalysisResults;
import org.surface.surface.core.engine.analysis.results.SnapshotAnalysisResults;
import org.surface.surface.core.engine.analysis.selectors.RevisionSelector;
import org.surface.surface.core.engine.analysis.setup.SetupEnvironmentAction;
import org.surface.surface.core.engine.metrics.api.MetricsManager;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class HistoryAnalyzer extends Analyzer {
    public static final String WORK_TREE = "WORK_TREE";
    private static final Logger LOGGER = LogManager.getLogger();
    private final String projectName;
    private final String repoLocation;
    private final boolean excludeWorkTree;
    private final RevisionSelector revisionSelector;
    private final SetupEnvironmentAction setupEnvironmentAction;

    public HistoryAnalyzer(String projectName, String repoLocation, String filesRegex, Set<Pattern> classifiedPatterns, MetricsManager metricsManager, boolean includeTests, boolean excludeWorkTree, RevisionSelector revisionSelector, SetupEnvironmentAction setupEnvironmentAction) {
        super(filesRegex, classifiedPatterns, metricsManager, includeTests);
        this.projectName = projectName;
        this.repoLocation = repoLocation;
        this.excludeWorkTree = excludeWorkTree;
        this.revisionSelector = revisionSelector;
        this.setupEnvironmentAction = setupEnvironmentAction;
    }

    public HistoryAnalysisResults analyze() throws Exception {
        Path tmpDirPath = setupEnvironmentAction.setupEnvironment();
        Path projectDirPath = Paths.get(tmpDirPath.toString(), projectName);

        SigIntHandler sigIntHandler = new SigIntHandler(tmpDirPath);
        Runtime.getRuntime().addShutdownHook(sigIntHandler);

        HistoryAnalysisResults analysisResults = new HistoryAnalysisResults(repoLocation);
        List<String> notProcessedCommits = new ArrayList<>();
        try (Git git = Git.open(projectDirPath.toFile())) {
            SnapshotAnalysisResults workTreeResults = null;
            // Analyze the current Work Tree if allowed
            Status stats = git.status().call();
            if (!excludeWorkTree && stats.getModified().size() > 0) {
                LOGGER.info("* Analyzing the current work tree in git repository {}", projectDirPath);
                workTreeResults = runSnapshotAnalysis(projectDirPath);
            }
            try {
                resetHard(git);
            } catch (GitAPIException e) {
                throw new RuntimeException("Failed to reset the state of git repository " + git.getRepository().getDirectory().getName(), e);
            }
            List<RevCommit> commits;
            try {
                commits = revisionSelector.selectRevisions(git);
            } catch (Exception e) {
                throw new RuntimeException("Failed to fetch the requested revisions from git repository " + projectDirPath, e);
            }
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
                    SnapshotAnalysisResults commitResults = runSnapshotAnalysis(projectDirPath);
                    analysisResults.addSnapshotAnalysisResults(commit.getName(), commitResults);
                }
            }
            if (workTreeResults != null) {
                analysisResults.addSnapshotAnalysisResults(WORK_TREE, workTreeResults);
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

    private SnapshotAnalysisResults runSnapshotAnalysis(Path projectDirPath) throws IOException {
        return new SnapshotAnalyzer(projectDirPath, getFilesRegex(), getClassifiedPatterns(), getMetricsManager(), isIncludeTests()).analyze();
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
