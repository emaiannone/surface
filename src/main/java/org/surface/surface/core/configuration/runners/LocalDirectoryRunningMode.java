package org.surface.surface.core.configuration.runners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.configuration.runners.results.LocalDirectoryRunResults;
import org.surface.surface.core.engine.analysis.SnapshotAnalyzer;
import org.surface.surface.core.engine.analysis.results.SnapshotAnalysisResults;
import org.surface.surface.core.engine.analysis.setup.CopySetupEnvironmentAction;
import org.surface.surface.core.engine.metrics.api.MetricsManager;
import org.surface.surface.core.engine.writers.FileWriter;

import java.io.IOException;
import java.nio.file.Path;

public class LocalDirectoryRunningMode extends SingleProjectRunningMode<LocalDirectoryRunResults> {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String CODE_NAME = "LOCAL_DIR";

    private final Path localDirPath;

    public LocalDirectoryRunningMode(Path localDirPath, Path workDirPath, FileWriter writer, MetricsManager metricsManager, String filesRegex, boolean includeTests) {
        super(workDirPath, writer, metricsManager, filesRegex, includeTests);
        if (localDirPath == null) {
            throw new IllegalArgumentException("The path to the target directory must not be null.");
        }
        if (metricsManager == null || metricsManager.getLoadedMetrics().size() == 0) {
            throw new IllegalArgumentException("The list of metrics to compute must not be null.");
        }
        this.localDirPath = localDirPath;
        setProjectName(localDirPath.getFileName().toString());
        setCodeName(CODE_NAME);
        setRunResults(new LocalDirectoryRunResults());
        setSetupEnvironmentAction(new CopySetupEnvironmentAction(getProjectName(), workDirPath, localDirPath));
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
