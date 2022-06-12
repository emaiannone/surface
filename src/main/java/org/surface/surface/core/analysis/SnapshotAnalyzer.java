package org.surface.surface.core.analysis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    public AnalysisResults analyze() throws IOException {
        ProjectMetricsCalculator projectMetricsCalculator = new ProjectMetricsCalculator(projectDirPath, getFilesRegex(), isIncludeTests());
        LOGGER.debug("* Metrics computation started");
        ProjectMetricsResults projectMetricsResults = projectMetricsCalculator.calculate(getMetricsManager());
        LOGGER.debug("* Metrics computation ended");
        LOGGER.trace("Metrics results:\n\t{}", projectMetricsResults.toString().replaceAll("\n", "\n\t"));
        // Pack it into a one-entry map for upholding the expected return type
        AnalysisResults analysisResults = new AnalysisResults();
        analysisResults.addProjectResult(projectDirPath.toString(), projectMetricsResults);
        return analysisResults;
    }
}
