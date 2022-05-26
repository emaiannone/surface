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
import org.surface.surface.common.Utils;
import org.surface.surface.common.selectors.RevisionSelector;
import org.surface.surface.core.explorers.JavaFilesExplorer;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;
import org.surface.surface.out.exporters.GitProjectResultsExporter;
import org.surface.surface.out.writers.Writer;
import org.surface.surface.out.writers.WriterFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class LocalGitAnalysisRunner extends AnalysisRunner<Map<String, ProjectMetricsResults>> {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Path workDirPath;
    private final RevisionSelector revisionSelector;

    public LocalGitAnalysisRunner(List<String> metrics, String target, Path outFilePath, String filesRegex, Path workDirPath, RevisionSelector revisionSelector) {
        super(metrics, target, outFilePath, filesRegex);
        this.workDirPath = workDirPath;
        this.revisionSelector = revisionSelector;
        Writer writer = new WriterFactory().getWriter(getOutFilePath());
        setResultsExporter(new GitProjectResultsExporter(writer));
    }

    @Override
    public void run() throws Exception {
        Map<String, ProjectMetricsResults> commitResults = new LinkedHashMap<>();
        Path targetDirPath = Paths.get(getTarget()).toAbsolutePath();
        File targetDir = targetDirPath.toFile();
        if (!Utils.isGitDirectory(targetDir)) {
            throw new IllegalStateException("* The target directory " + targetDir + " does not exist or is not a git directory.");
        }

        // Copy the repository into the Working Directory
        Path tmpDirPath = Paths.get(workDirPath.toString(), "SURFACE_TMP");
        clearTmpDirectory(tmpDirPath);
        Path repoDirPath = Paths.get(tmpDirPath.toString(), targetDir.getName());
        repoDirPath.toFile().mkdirs();
        // In case of interrupts, clear the temporary directory
        Runtime.getRuntime().addShutdownHook(new SigIntHandler(tmpDirPath));
        FileUtils.copyDirectory(targetDir, repoDirPath.toFile());

        List<String> notProcessedCommits = new ArrayList<>();
        try (Git git = Git.open(repoDirPath.toFile())) {
            List<RevCommit> commits;
            try {
                resetHard(git);
                commits = revisionSelector.selectRevisions(git);
            } catch (Exception e) {
                throw new Exception("Failed to fetch the history of git repository " + targetDir, e);
            }
            Collections.reverse(commits);
            int numCommits = commits.size();
            LOGGER.info("* Going to analyze {} commits in git repository {}", numCommits, targetDir);
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
                    commitResults.put(commit.getName(), super.analyze(repoDirPath, files));
                }
            }
        } catch (IOException e) {
            throw new IOException("* Could not open git repository " + targetDir, e);
        } finally {
            if (notProcessedCommits.size() > 0) {
                LOGGER.warn("* Failed to process the following commits: " + notProcessedCommits);
            }
            clearTmpDirectory(tmpDirPath);
        }
        exportResults(commitResults);
    }

    private void resetHard(Git git) throws Exception {
        try {
            git.reset().setMode(ResetCommand.ResetType.HARD).call();
        } catch (Exception e) {
            LOGGER.error("* Could not reset the state of git repository " + git.getRepository().getDirectory().getName(), e);
            throw e;
        }
    }

    private void clearTmpDirectory(Path tmpDirPath) throws IOException {
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
                clearTmpDirectory(tmpDirPath);
            } catch (IOException e) {
                LOGGER.warn("* Could not delete the working directory");
            }
        }
    }
}
