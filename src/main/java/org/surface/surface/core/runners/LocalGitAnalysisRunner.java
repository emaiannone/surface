package org.surface.surface.core.runners;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.surface.surface.common.RevisionMode;
import org.surface.surface.common.Utils;
import org.surface.surface.common.selectors.RevisionsSelector;
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

    // TODO Don't like, should be made a class with subclasses, each with an overriden method that acts like selectRevision
    private final Pair<RevisionMode, String> revision;

    public LocalGitAnalysisRunner(List<String> metrics, String target, Path outFilePath, String filesRegex, Pair<RevisionMode, String> revision) {
        super(metrics, target, outFilePath, filesRegex);
        this.revision = revision;
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
        try (Git git = Git.open(targetDir)) {
            // Preventive stash not to lose any local change!
            try {
                Set<String> uncommittedChanges = git.status().call().getUncommittedChanges();
                if (uncommittedChanges.size() > 0) {
                    LOGGER.debug("Successfully Created a stash of the current working tree in git repository " + targetDir);
                    git.stashCreate().call();
                }
            } catch (GitAPIException e) {
                LOGGER.error("* Could not stash the current working tree in git repository " + targetDir, e);
            }

            String defaultBranchName = git.getRepository().getBranch();
            List<RevCommit> commits;
            try {
                git.reset().setMode(ResetCommand.ResetType.HARD).call();
                commits = RevisionsSelector.selectRevisions(git, revision);
            } catch (Exception e) {
                throw new Exception("Failed to fetch the history of git repository " + targetDir, e);
            }
            LOGGER.info("* Going to analyze {} commits in git repository {}", commits.size(), targetDir);
            try {
                for (RevCommit commit : commits) {
                    LOGGER.trace("Analyzing commit {}", commit.getName());
                    // DEBUG remove
                    if (true)
                        continue;
                    try {
                        git.checkout().setName(commit.getName()).call();
                    } catch (GitAPIException e) {
                        LOGGER.warn("* Failed checkout to commit " + commit.getName() + " in git repository " + targetDir + ": ignoring");
                        continue;
                    }
                    List<Path> files = getFilesRegex() == null ?
                            JavaFilesExplorer.selectFiles(targetDirPath) :
                            JavaFilesExplorer.selectFiles(targetDirPath, getFilesRegex());
                    commitResults.put(commit.getName(), super.analyze(targetDirPath, files));
                    try {
                        git.reset().setMode(ResetCommand.ResetType.HARD).call();
                    } catch (GitAPIException e) {
                        LOGGER.error("* Could not reset the state of git repository " + targetDir, e);
                        break;
                    }
                }
            } finally {
                // Restore everything to the initial state
                try {
                    git.checkout().setName(defaultBranchName).call();
                    if (git.stashList().call().size() > 0) {
                        git.stashApply().call();
                        git.stashDrop().call();
                        LOGGER.debug("* Successfully restored the previous state of the git repository " + targetDir);
                    }
                } catch (GitAPIException e) {
                    LOGGER.error("* Failed to restore previous state of the git repository " + targetDir);
                }
            }
        } catch (IOException e) {
            throw new IOException("* Could not open git repository " + targetDir, e);
        }
        exportResults(commitResults);
    }
}
