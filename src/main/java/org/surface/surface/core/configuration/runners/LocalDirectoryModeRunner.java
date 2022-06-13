package org.surface.surface.core.configuration.runners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.configuration.runners.results.LocalDirectoryRunResults;
import org.surface.surface.core.engine.analysis.SnapshotAnalyzer;
import org.surface.surface.core.engine.analysis.results.SnapshotAnalysisResults;
import org.surface.surface.core.engine.metrics.api.MetricsManager;
import org.surface.surface.core.engine.writers.FileWriter;

import java.io.IOException;
import java.nio.file.Path;

public class LocalDirectoryModeRunner extends ModeRunner<LocalDirectoryRunResults> {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String CODE_NAME = "LOCAL_DIR";

    private final Path localDirPath;

    public LocalDirectoryModeRunner(Path localDirPath, MetricsManager metricsManager, FileWriter writer, String filesRegex, boolean includeTests) {
        super(metricsManager, writer, filesRegex, includeTests);
        this.localDirPath = localDirPath;
        setRunResults(new LocalDirectoryRunResults());
        setCodeName(CODE_NAME);
    }

    @Override
    public void run() throws IOException {
        if (!localDirPath.toFile().isDirectory()) {
            throw new IllegalStateException("The target directory does not exist or is not a directory.");
        }
        SnapshotAnalyzer snapshotAnalyzer = new SnapshotAnalyzer(localDirPath, getFilesRegex(), getMetricsManager(), isIncludeTests());
        SnapshotAnalysisResults analysisResults = snapshotAnalyzer.analyze();
        LocalDirectoryRunResults runResults = getRunResults();
        runResults.setAnalysisResults(analysisResults);
        exportResults(runResults);
        LOGGER.info("* Exported results to {}", getWriter().getOutFile());
    }
}
