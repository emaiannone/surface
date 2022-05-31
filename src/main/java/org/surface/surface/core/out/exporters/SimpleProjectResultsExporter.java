package org.surface.surface.core.out.exporters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;
import org.surface.surface.core.out.writers.FileWriter;

import java.io.IOException;
import java.util.Map;

public class SimpleProjectResultsExporter extends ResultsExporter<ProjectMetricsResults> {
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void export(ProjectMetricsResults projectMetricsResults, FileWriter writer) throws IOException {
        Map<String, Object> content = projectMetricsResults.toMap();
        LOGGER.debug("Results exported: {}", content);
        writer.write(content);
    }
}
