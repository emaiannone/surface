package org.surface.surface.core.runners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.analysis.SnapshotAnalyzer;
import org.surface.surface.core.metrics.api.Metric;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;
import org.surface.surface.core.out.exporters.SimpleProjectResultsExporter;
import org.surface.surface.core.out.writers.FileWriter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class LocalDirectoryModeRunner extends ModeRunner<ProjectMetricsResults> {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String CODE_NAME = "LOCAL_DIR";

    private final Path localDirPath;

    public LocalDirectoryModeRunner(Path localDirPath, List<Metric<?, ?>> metrics, FileWriter writer, String filesRegex) {
        super(metrics, writer, filesRegex);
        this.localDirPath = localDirPath;
        setCodeName(CODE_NAME);
        setResultsExporter(new SimpleProjectResultsExporter());
    }

    @Override
    public void run() throws IOException {
        if (!localDirPath.toFile().isDirectory()) {
            throw new IllegalStateException("The target directory does not exist or is not a directory.");
        }
        SnapshotAnalyzer snapshotAnalyzer = new SnapshotAnalyzer(localDirPath, getFilesRegex(), getMetrics());
        ProjectMetricsResults projectMetricsResults = snapshotAnalyzer.analyze();
        exportResults(projectMetricsResults);
    }
}
