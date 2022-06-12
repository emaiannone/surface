package org.surface.surface.core.analysis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.metrics.api.MetricsManager;
import org.surface.surface.core.metrics.api.ProjectMetricsCalculator;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class SnapshotAnalyzer extends Analyzer {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Path projectDirPath;

    public SnapshotAnalyzer(Path projectDirPath, String filesRegex, MetricsManager metricsManager, boolean includeTests) {
        super(filesRegex, metricsManager, includeTests);
        this.projectDirPath = projectDirPath;
    }

    public Map<String, ProjectMetricsResults> analyze() throws IOException {
        ProjectMetricsCalculator projectMetricsCalculator = new ProjectMetricsCalculator(projectDirPath, getFilesRegex(), isIncludeTests());
        LOGGER.debug("* Metrics computation started");
        ProjectMetricsResults projectMetricsResults = projectMetricsCalculator.calculate(getMetricsManager());
        LOGGER.debug("* Metrics computation ended");
        LOGGER.trace("Metrics results:\n\t{}", projectMetricsResults.toString().replaceAll("\n", "\n\t"));
        // Pack it into a one-entry map for upholding the expected return type
        Map<String, ProjectMetricsResults> results = new LinkedHashMap<>();
        results.put(projectDirPath.toString(), projectMetricsResults);
        return results;
    }
}
