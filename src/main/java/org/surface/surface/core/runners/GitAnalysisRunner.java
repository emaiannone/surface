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
import org.surface.surface.core.explorers.JavaFilesExplorer;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public abstract class GitAnalysisRunner extends AnalysisRunner<Map<String, ProjectMetricsResults>> {
    private static final Logger LOGGER = LogManager.getLogger();

    public static final String SURFACE_TMP = "SURFACE_TMP";

    private final Path workDirPath;
    private final RevisionSelector revisionSelector;

    public GitAnalysisRunner(List<String> metrics, String target, Path outFilePath, String filesRegex, Path workDirPath, RevisionSelector revisionSelector) {
        super(metrics, target, outFilePath, filesRegex);
        this.workDirPath = workDirPath;
        this.revisionSelector = revisionSelector;
    }

    public String getProjectName() {
        return getTargetPath().getFileName().toString();
    }

    public Path getWorkDirPath() {
        return workDirPath;
    }

    protected Path getTmpDirPath() {
        return Paths.get(workDirPath.toString(), SURFACE_TMP);
    }

    protected Path getRepoDirPath() {
        return Paths.get(getTmpDirPath().toString(), getProjectName());
    }

    public List<RevCommit> selectRevisions(Git git) throws GitAPIException, IOException {
        return revisionSelector.selectRevisions(git);
    }

    protected abstract Path prepareTmpDir();

    @Override
    public void run() throws Exception {
        Map<String, ProjectMetricsResults> allResults = new LinkedHashMap<>();

        // Depends on the specific thing to do
        Path tmpDirPath = prepareTmpDir();
        Path repoDirPath = Paths.get(tmpDirPath.toString(), getProjectName());

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
                commits = selectRevisions(git);
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
                    List<Path> files = JavaFilesExplorer.selectFiles(repoDirPath, getFilesRegex());
                    LOGGER.debug("Java files found: {}", files);
                    progressBar.setExtraMessage("Inspecting " + commit.getName().substring(0, 8) + " (" + files.size() + " files)");
                    progressBar.step();
                    allResults.put(commit.getName(), super.analyze(repoDirPath, files));
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

    protected void resetHard(Git git) throws GitAPIException {
        git.reset().setMode(ResetCommand.ResetType.HARD).call();
    }

    protected void deleteTmpDirectory(Path tmpDirPath) throws IOException {
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
