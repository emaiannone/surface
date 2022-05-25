package org.surface.surface.core.runner;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.explorer.JavaFilesExplorer;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class LocalDirectoryAnalysisRunner extends AnalysisRunner {
    private static final Logger LOGGER = LogManager.getLogger();

    public LocalDirectoryAnalysisRunner(List<String> metrics, String target, Path outFilePath, String filesRegex) {
        super(metrics, target, outFilePath, filesRegex);
    }

    @Override
    public void run() throws IOException {
        Path targetDir = Paths.get(getTarget()).toAbsolutePath();
        File targetDirFile = targetDir.toFile();
        if (!targetDirFile.exists() && !targetDirFile.isDirectory()) {
            throw new IllegalStateException("The target directory does not exist or is not a directory.");
        }
        List<Path> files = getFilesRegex() == null ?
                JavaFilesExplorer.selectFiles(targetDir) :
                JavaFilesExplorer.selectFiles(targetDir, getFilesRegex());
        ProjectMetricsResults projectMetricsResults = super.analyze(targetDir, files);
        exportProjectResults(projectMetricsResults, FilenameUtils.getExtension(getOutFilePath().toFile().getName()));
    }
}
