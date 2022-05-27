package org.surface.surface.core.runners;

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
import org.surface.surface.common.selectors.RevisionSelector;
import org.surface.surface.core.analysis.ProjectAnalyzer;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public abstract class GitModeRunner extends ModeRunner<Map<String, ProjectMetricsResults>> {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final String SURFACE_TMP = "SURFACE_TMP";

    private final Path workDirPath;
    private final RevisionSelector revisionSelector;

    GitModeRunner(List<String> metrics, String target, Path outFilePath, String filesRegex, Path workDirPath, RevisionSelector revisionSelector) {
        super(metrics, target, outFilePath, filesRegex);
        this.workDirPath = workDirPath;
        this.revisionSelector = revisionSelector;
    }

    private String getProjectName() {
        return getTargetPath().getFileName().toString();
    }

    public Path getWorkDirPath() {
        return workDirPath;
    }

    Path getTmpDirPath() {
        return Paths.get(workDirPath.toString(), SURFACE_TMP);
    }

    Path getRepoDirPath() {
        return Paths.get(getTmpDirPath().toString(), getProjectName());
    }

    protected abstract Path prepareTmpDir();

    @Override
    public void run() throws Exception {
        // Depends on the specific thing to do
        Path tmpDirPath = prepareTmpDir();
        Path repoDirPath = Paths.get(tmpDirPath.toString(), getProjectName());

        // TODO Extract the logic (from here? with all the private methods?) that will be used many times by FlexibleMode into HistoryAnalyzer.
        Map<String, ProjectMetricsResults> allResults = new LinkedHashMap<>();

        // In case of interrupts, clear the temporary directory
        SigIntHandler sigIntHandler = new SigIntHandler(tmpDirPath);
        Runtime.getRuntime().addShutdownHook(sigIntHandler);

        List<String> notProcessedCommits = new ArrayList<>();
        try (Git git = Git.open(repoDirPath.toFile())) {
            List<RevCommit> commits;
            try {
                resetHard(git);
            } catch (GitAPIException e) {
                throw new RuntimeException("The reset of git repository " + git.getRepository().getDirectory().getName() + "failed", e);
            }
            try {
                commits = revisionSelector.selectRevisions(git);
            } catch (Exception e) {
                throw new Exception("Failed to fetch the required revisions from git repository " + repoDirPath, e);
            }
            Collections.reverse(commits);
            int numCommits = commits.size();
            LOGGER.info("* Going to analyze {} commits in git repository {}", numCommits, repoDirPath);
            try (ProgressBar progressBar = new ProgressBarBuilder()
                    .setTaskName("Analyses progress")
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
                    ProjectAnalyzer projectAnalyzer = new ProjectAnalyzer(repoDirPath, getFilesRegex(), getMetrics());
                    ProjectMetricsResults projectMetricsResults = projectAnalyzer.analyze();
                    allResults.put(commit.getName(), projectMetricsResults);
                }
            }
        } catch (IOException e) {
            throw new IOException("Could not open git repository " + repoDirPath, e);
        } finally {
            if (notProcessedCommits.size() > 0) {
                LOGGER.warn("* Failed to process the following commits: " + notProcessedCommits);
            }
            deleteTmpDirectory(tmpDirPath);
            Runtime.getRuntime().removeShutdownHook(sigIntHandler);
        }
        exportResults(allResults);
    }

    private void resetHard(Git git) throws GitAPIException {
        git.reset().setMode(ResetCommand.ResetType.HARD).call();
    }

    void deleteTmpDirectory(Path tmpDirPath) throws IOException {
        if (tmpDirPath.toFile().exists()) {
            FileUtils.deleteDirectory(tmpDirPath.toFile());
        }
    }

    private class SigIntHandler extends Thread {
        private final Path tmpDirPath;

        private SigIntHandler(Path tmpDirPath) {
            this.tmpDirPath = tmpDirPath;
        }

        @Override
        public void run() {
            try {
                deleteTmpDirectory(tmpDirPath);
            } catch (IOException e) {
                LOGGER.warn("* Could not delete the working directory");
            }
        }
    }
}
