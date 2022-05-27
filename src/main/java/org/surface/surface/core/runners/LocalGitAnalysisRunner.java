package org.surface.surface.core.runners;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.common.Utils;
import org.surface.surface.common.selectors.RevisionSelector;
import org.surface.surface.out.exporters.GitProjectResultsExporter;
import org.surface.surface.out.writers.Writer;
import org.surface.surface.out.writers.WriterFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class LocalGitAnalysisRunner extends GitAnalysisRunner {
    private static final Logger LOGGER = LogManager.getLogger();

    public LocalGitAnalysisRunner(List<String> metrics, String target, Path outFilePath, String filesRegex, Path workDirPath, RevisionSelector revisionSelector) {
        super(metrics, target, outFilePath, filesRegex, workDirPath, revisionSelector);
        Writer writer = new WriterFactory().getWriter(getOutFilePath());
        setResultsExporter(new GitProjectResultsExporter(writer, null));
    }

    @Override
    protected Path prepareTmpDir() {
        // Ensure the target is a git repository, and then copy it into the temporary directory
        Path targetDirPath = getTargetPath();
        if (!Utils.isGitDirectory(targetDirPath.toFile())) {
            throw new IllegalStateException("The target directory " + targetDirPath + " does not exist or is not a git directory.");
        }
        Path tmpDirPath = getTmpDirPath();
        try {
            deleteTmpDirectory(tmpDirPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete the working directory " + tmpDirPath +
                    ". Please, try again or select a new destination.", e);
        }
        Path repoDirPath = getRepoDirPath();
        repoDirPath.toFile().mkdirs();
        try {
            FileUtils.copyDirectory(targetDirPath.toFile(), repoDirPath.toFile());
        } catch (IOException e) {
            throw new RuntimeException("Failed to copy the git repository into " + repoDirPath +
                    ". Please, try again or select a new destination.", e);
        }
        return tmpDirPath;
    }
}
