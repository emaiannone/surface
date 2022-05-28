package org.surface.surface.core.runners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.common.Utils;
import org.surface.surface.core.analysis.SnapshotAnalyzer;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;
import org.surface.surface.out.exporters.SimpleProjectResultsExporter;
import org.surface.surface.out.writers.Writer;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class LocalDirectoryModeRunner extends ModeRunner<ProjectMetricsResults> {
    private static final Logger LOGGER = LogManager.getLogger();

    public LocalDirectoryModeRunner(List<String> metrics, String target, Path outFilePath, String filesRegex) {
        super(metrics, target, outFilePath, filesRegex);
        Writer writer = Writer.newWriter(getOutFilePath());
        setResultsExporter(new SimpleProjectResultsExporter(writer));
    }

    @Override
    public void run() throws IOException {
        Path dirPath = Paths.get(getTarget()).toAbsolutePath();
        if (!Utils.isDirectory(dirPath.toFile())) {
            throw new IllegalStateException("The target directory does not exist or is not a directory.");
        }

        SnapshotAnalyzer snapshotAnalyzer = new SnapshotAnalyzer(dirPath, getDefaultFilesRegex(), getMetrics());
        ProjectMetricsResults projectMetricsResults = snapshotAnalyzer.analyze();
        exportResults(projectMetricsResults);
    }
}
