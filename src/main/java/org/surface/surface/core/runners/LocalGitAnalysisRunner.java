package org.surface.surface.core.runners;

import me.tongfei.progressbar.ConsoleProgressBarConsumer;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;
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

    private final RevisionSelector revisionSelector;

    public LocalGitAnalysisRunner(List<String> metrics, String target, Path outFilePath, String filesRegex, RevisionSelector revisionSelector) {
        super(metrics, target, outFilePath, filesRegex);
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
        
        String stashName = "SURFACE_" + UUID.randomUUID();
        try (Git git = Git.open(targetDir)) {
            // Preventive stash not to lose any local change!
            try {
                unlock(git);
                Set<String> uncommittedChanges = git.status().call().getUncommittedChanges();
                if (uncommittedChanges.size() > 0) {
                    LOGGER.info("* Creating stash to keep safe the working tree in git repository " + targetDir);
                    git.stashCreate().setRef(stashName).call();
                }
            } catch (GitAPIException e) {
                LOGGER.error("* Could not create a stash of the working tree in git repository " + targetDir, e);
            }
            String initialHead = git.getRepository().getBranch();
            Runtime.getRuntime().addShutdownHook(new SigIntHandler(git, initialHead, stashName));

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
                    .build()) {
                for (RevCommit commit : commits) {
                    try {

                        git.checkout().setName(commit.getName()).call();
                    } catch (GitAPIException e) {
                        // TODO In addition, collect these warns and print them at the end of the loop
                        LOGGER.warn("* Failed checkout to commit " + commit.getName() + " in git repository " + targetDir + ": ignoring");
                        continue;
                    }
                    List<Path> files = JavaFilesExplorer.selectFiles(targetDirPath, getFilesRegex());
                    LOGGER.debug("Java files found: {}", files);
                    progressBar.setExtraMessage("Inspecting " + commit.getName().substring(0, 8) + " (" + files.size() + " files)");
                    progressBar.step();
                    commitResults.put(commit.getName(), super.analyze(targetDirPath, files));
                    try {
                        resetHard(git);
                    } catch (GitAPIException e) {
                        break;
                    }
                }
            } finally {
                // Restore everything to the initial state
                restore(git, initialHead, stashName);
            }
        } catch (IOException e) {
            throw new IOException("* Could not open git repository " + targetDir, e);
        }
        exportResults(commitResults);
    }

    private void unlock(Git git) {
        for (File file : git.getRepository().getDirectory().listFiles()) {
            if (file.getName().endsWith(".lock")) {
                LOGGER.warn("* Found {} file: deleting it to operate safely", file.getName());
                file.delete();
            }
        }
    }

    private void resetHard(Git git) throws Exception {
        try {
            git.reset().setMode(ResetCommand.ResetType.HARD).call();
        } catch (Exception e) {
            LOGGER.error("* Could not reset the state of git repository " + git.getRepository().getDirectory().getName(), e);
            throw e;
        }
    }

    private void restore(Git git, String commit, String stashName) throws Exception {
        try {
            unlock(git);
            git.checkout().setName(commit).call();
            if (git.stashList().call().size() > 0) {
                git.stashApply().setStashRef(stashName).call();
                git.stashDrop().call();
            }
            LOGGER.info("* Successfully restored the previous state of the git repository " + git.getRepository().getDirectory().toPath());
        } catch (Exception e) {
            LOGGER.error("* Failed to restore previous state of the git repository " + git.getRepository().getDirectory().toPath());
            throw e;
        }
    }

    private class SigIntHandler extends Thread {
        private final Git git;
        private final String initialHead;
        private final String stashName;

        private SigIntHandler(Git git, String initialHead, String stashName) {
            this.git = git;
            this.initialHead = initialHead;
            this.stashName = stashName;
        }

        @Override
        public void run() {
            System.out.println();
            LOGGER.warn("* Received shutdown signal");
            try {
                restore(git, initialHead, stashName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
