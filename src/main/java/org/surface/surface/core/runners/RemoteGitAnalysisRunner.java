package org.surface.surface.core.runners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.surface.surface.common.selectors.RevisionSelector;
import org.surface.surface.out.exporters.GitProjectResultsExporter;
import org.surface.surface.out.writers.Writer;
import org.surface.surface.out.writers.WriterFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class RemoteGitAnalysisRunner extends GitAnalysisRunner {
    private static final Logger LOGGER = LogManager.getLogger();

    RemoteGitAnalysisRunner(List<String> metrics, String target, Path outFilePath, String filesRegex, Path workDirPath, RevisionSelector revisionSelector) {
        super(metrics, target, outFilePath, filesRegex, workDirPath, revisionSelector);
        Writer writer = new WriterFactory().getWriter(getOutFilePath());
        setResultsExporter(new GitProjectResultsExporter(writer, target));
    }

    @Override
    protected Path prepareTmpDir() {
        // Clone the repository into the temporary directory
        Path tmpDirPath = getTmpDirPath();
        try {
            deleteTmpDirectory(tmpDirPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete the working directory " + tmpDirPath +
                    ". Please, try again or select a new destination.", e);
        }
        Path repoDirPath = getRepoDirPath();
        repoDirPath.toFile().mkdirs();
        LOGGER.info("* Cloning {} into {}", getTarget(), repoDirPath);
        try (Git git = Git.cloneRepository()
                     .setURI(getTarget())
                     .setDirectory(repoDirPath.toFile())
                     .call()) {
            LOGGER.info("* Clone successful");
        } catch (GitAPIException e) {
            throw new RuntimeException("Failed to clone the remote git repository from " + getTarget() +
                    " into " + tmpDirPath +
                    ". Please, try again or select a new destination.", e);
        }
        return tmpDirPath;
    }
}
