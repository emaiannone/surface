package org.surface.surface.out.exporters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;
import org.surface.surface.out.writers.Writer;

import java.io.IOException;
import java.util.Map;

public class SimpleProjectResultsExporter extends ResultsExporter<ProjectMetricsResults> {
    private static final Logger LOGGER = LogManager.getLogger();

    public SimpleProjectResultsExporter(Writer writer) {
        super(writer);
    }

    @Override
    public void export(ProjectMetricsResults projectMetricsResults) throws IOException {
        Map<String, Object> content = projectMetricsResults.toMap();
        LOGGER.debug("Results exported: {}", content);
        getResultsWriter().write(content);
    }
}
