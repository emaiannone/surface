package org.surface.surface.core.analysis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.analysis.results.SnapshotAnalysisResults;
import org.surface.surface.core.metrics.api.MetricsManager;
import org.surface.surface.core.metrics.api.ProjectMetricsCalculator;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;

import java.io.IOException;
import java.nio.file.Path;

public class SnapshotAnalyzer extends Analyzer {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Path projectDirPath;

    public SnapshotAnalyzer(Path projectDirPath, String filesRegex, MetricsManager metricsManager, boolean includeTests) {
        super(filesRegex, metricsManager, includeTests);
        this.projectDirPath = projectDirPath;
    }

    public SnapshotAnalysisResults analyze() throws IOException {
        ProjectMetricsCalculator projectMetricsCalculator = new ProjectMetricsCalculator(projectDirPath, getFilesRegex(), isIncludeTests());
        LOGGER.debug("* Metrics computation started");
        ProjectMetricsResults projectMetricsResults = projectMetricsCalculator.calculate(getMetricsManager());
        LOGGER.debug("* Metrics computation ended");
        LOGGER.trace("Metrics results:\n\t{}", projectMetricsResults.toString().replaceAll("\n", "\n\t"));
        return new SnapshotAnalysisResults(projectDirPath.toString(), projectMetricsResults);
    }
}
