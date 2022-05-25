package org.surface.surface.core.runners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.explorers.JavaFilesExplorer;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;
import org.surface.surface.out.exporters.LocalDirectoryResultsExporter;
import org.surface.surface.out.writers.Writer;
import org.surface.surface.out.writers.WriterFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class LocalDirectoryAnalysisRunner extends AnalysisRunner {
    private static final Logger LOGGER = LogManager.getLogger();

    public LocalDirectoryAnalysisRunner(List<String> metrics, String target, Path outFilePath, String filesRegex) {
        super(metrics, target, outFilePath, filesRegex);
        Writer writer = new WriterFactory().getWriter(getOutFilePath());
        setResultsExporter(new LocalDirectoryResultsExporter(writer));
    }

    @Override
    public void run() throws IOException {
        Path targetDirPath = Paths.get(getTarget()).toAbsolutePath();
        File targetDir = targetDirPath.toFile();
        if (!targetDir.exists() && !targetDir.isDirectory()) {
            throw new IllegalStateException("The target directory does not exist or is not a directory.");
        }
        List<Path> files = getFilesRegex() == null ?
                JavaFilesExplorer.selectFiles(targetDirPath) :
                JavaFilesExplorer.selectFiles(targetDirPath, getFilesRegex());
        ProjectMetricsResults projectMetricsResults = super.analyze(targetDirPath, files);
        exportResults(projectMetricsResults);
    }
}
