package org.surface.surface.core.runners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.analysis.SnapshotAnalyzer;
import org.surface.surface.core.metrics.api.MetricsManager;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;
import org.surface.surface.core.out.exporters.SimpleProjectResultsExporter;
import org.surface.surface.core.out.writers.FileWriter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class LocalDirectoryModeRunner extends ModeRunner<ProjectMetricsResults> {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String CODE_NAME = "LOCAL_DIR";

    private final Path localDirPath;

    public LocalDirectoryModeRunner(Path localDirPath, MetricsManager metricsManager, FileWriter writer, String filesRegex, boolean includeTests) {
        super(metricsManager, writer, filesRegex, includeTests);
        this.localDirPath = localDirPath;
        setCodeName(CODE_NAME);
        setResultsExporter(new SimpleProjectResultsExporter());
    }

    @Override
    public void run() throws IOException {
        if (!localDirPath.toFile().isDirectory()) {
            throw new IllegalStateException("The target directory does not exist or is not a directory.");
        }
        SnapshotAnalyzer snapshotAnalyzer = new SnapshotAnalyzer(localDirPath, getFilesRegex(), getMetricsManager(), isIncludeTests());
        // TODO Wrap this into a different object, that exposes method to query it from clients without depending on metrics pakcage
        Map<String, ProjectMetricsResults> results = snapshotAnalyzer.analyze().getResults();
        exportResults(results.get(localDirPath.toString()));
        LOGGER.info("* Exported results to {}", getWriter().getOutFile());
    }
}
