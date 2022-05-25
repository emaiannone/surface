package org.surface.surface.core.runners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.surface.surface.common.Utils;
import org.surface.surface.common.filters.RevisionFilter;
import org.surface.surface.core.explorers.JavaFilesExplorer;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;
import org.surface.surface.out.exporters.GitProjectResultsExporter;
import org.surface.surface.out.writers.Writer;
import org.surface.surface.out.writers.WriterFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LocalGitAnalysisRunner extends AnalysisRunner<Map<String, ProjectMetricsResults>> {
    private static final Logger LOGGER = LogManager.getLogger();

    private final RevisionFilter revisionFilter;

    public LocalGitAnalysisRunner(List<String> metrics, String target, Path outFilePath, String filesRegex, RevisionFilter revisionFilter) {
        super(metrics, target, outFilePath, filesRegex);
        this.revisionFilter = revisionFilter;
        Writer writer = new WriterFactory().getWriter(getOutFilePath());
        setResultsExporter(new GitProjectResultsExporter(writer));
    }

    public RevisionFilter getRevisionFilter() {
        return revisionFilter;
    }

    @Override
    public void run() throws IOException {
        Map<String, ProjectMetricsResults> commitResults = new LinkedHashMap<>();
        Path targetDirPath = Paths.get(getTarget()).toAbsolutePath();
        File targetDir = targetDirPath.toFile();
        if (!Utils.isGitDirectory(targetDir)) {
            throw new IllegalStateException("The target directory does not exist or is not a git directory.");
        }
        try (Git git = Git.open(targetDir)) {
            // Preventive stash not to lose any local change!
            try {
                Set<String> uncommittedChanges = git.status().call().getUncommittedChanges();
                if (uncommittedChanges.size() > 0) {
                    git.stashCreate().call();
                }
            } catch (GitAPIException e) {
                LOGGER.info("Could not stash the current working tree in git repository " + targetDir, e);
            }

            // TODO If HEAD or SINGLE modes do not take the entire history
            String defaultBranch = git.getRepository().getBranch();
            Iterable<RevCommit> commits = null;
            try {
                git.reset().setMode(ResetCommand.ResetType.HARD).call();
                commits = git.log().call();
            } catch (GitAPIException e) {
                throw new IOException("Could not fetch history of git repository " + targetDir, e);
            }

            if (commits != null) {
                try {
                    for (RevCommit commit : commits) {
                        // TODO Filter commits depending on the filter
                        LOGGER.debug("Analyzing commit {}", commit.getName());
                        try {
                            git.checkout().setName(commit.getName()).call();
                        } catch (GitAPIException e) {
                            LOGGER.info("Could not checkout to commit " + commit.getName() + " in git repository " + targetDir, e);
                            continue;
                        }
                        List<Path> files = getFilesRegex() == null ?
                                JavaFilesExplorer.selectFiles(targetDirPath) :
                                JavaFilesExplorer.selectFiles(targetDirPath, getFilesRegex());
                        commitResults.put(commit.getName(), super.analyze(targetDirPath, files));
                        try {
                            git.reset().setMode(ResetCommand.ResetType.HARD).call();
                        } catch (GitAPIException e) {
                            LOGGER.info("Could not restore the state of the git repository " + targetDir, e);
                            break;
                        }
                    }
                } finally {
                    try {
                        git.checkout().setName(defaultBranch).call();
                        git.stashApply().call();
                        git.stashDrop().call();
                        LOGGER.info("* Successfully restored the previous state of the git repository " + targetDir);
                    } catch (GitAPIException e) {
                        LOGGER.error("* Could not restore the previous state of the git repository " + targetDir);
                    }
                }
            }
        } catch (IOException e) {
            throw new IOException("Could not open git repository " + targetDir, e);
        }
        exportResults(commitResults);
    }
}
