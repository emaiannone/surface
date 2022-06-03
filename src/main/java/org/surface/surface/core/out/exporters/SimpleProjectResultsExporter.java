package org.surface.surface.core.out.exporters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;

import java.util.Map;

public class SimpleProjectResultsExporter extends ResultsExporter<ProjectMetricsResults> {
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public Map<String, Object> export(ProjectMetricsResults projectMetricsResults) {
        return projectMetricsResults.toMap();
    }
}
