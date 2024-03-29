package org.surface.surface.core.configuration.runners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.engine.analysis.SnapshotAnalyzer;
import org.surface.surface.core.engine.analysis.results.SnapshotAnalysisResults;
import org.surface.surface.core.engine.analysis.setup.CopySetupEnvironmentAction;
import org.surface.surface.core.engine.metrics.api.MetricsManager;
import org.surface.surface.core.exporters.RunResultsExporter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.regex.Pattern;

public class LocalDirectoryRunningMode extends SingleProjectRunningMode {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String CODE_NAME = "LOCAL_DIR";

    private final Path localDirPath;

    public LocalDirectoryRunningMode(Path localDirPath, Path workDirPath, RunResultsExporter runResultsExporter, Set<Pattern> classifiedPatterns, MetricsManager metricsManager, String filesRegex, boolean includeTests) {
        super(workDirPath, runResultsExporter, classifiedPatterns, metricsManager, filesRegex, includeTests);
        if (localDirPath == null) {
            throw new IllegalArgumentException("The path to the target directory must not be null.");
        }
        if (metricsManager == null || metricsManager.getLoadedMetrics().size() == 0) {
            throw new IllegalArgumentException("The list of metrics to compute must not be null.");
        }
        this.localDirPath = localDirPath;
        setProjectName(localDirPath.getFileName().toString());
        setCodeName(CODE_NAME);
        setSetupEnvironmentAction(new CopySetupEnvironmentAction(getProjectName(), workDirPath, localDirPath));
    }

    @Override
    public void run() throws IOException {
        if (!localDirPath.toFile().isDirectory()) {
            throw new IllegalStateException("The target directory does not exist or is not a directory.");
        }
        SnapshotAnalyzer snapshotAnalyzer = new SnapshotAnalyzer(localDirPath, getFilesRegex(), getClassifiedPatterns(), getMetricsManager(), isIncludeTests());
        SnapshotAnalysisResults analysisResults = snapshotAnalyzer.analyze();
        addAnalysisResults(getProjectName(), analysisResults);
        exportResults();
        LOGGER.info("* Exported results to {}", getFormatter().getOutFile());
    }
}
