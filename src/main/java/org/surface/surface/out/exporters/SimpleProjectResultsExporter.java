package org.surface.surface.out.exporters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.metrics.results.ClassMetricsResults;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;
import org.surface.surface.out.writers.Writer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SimpleProjectResultsExporter extends ResultsExporter<ProjectMetricsResults> {
    private static final Logger LOGGER = LogManager.getLogger();

    public SimpleProjectResultsExporter(Writer writer) {
        super(writer);
    }

    @Override
    public void export(ProjectMetricsResults projectMetricsResults) throws IOException {
        Map<String, Object> content = new LinkedHashMap<>();
        content.put("projectPath", projectMetricsResults.getProjectRoot().toString());
        content.put("metrics", projectMetricsResults.getProjectMetrics());
        List<Map<?, ?>> classes = new ArrayList<>();
        for (ClassMetricsResults classMetricsResult : projectMetricsResults.getClassMetricsResults()) {
            Map<String, Object> clazz = new LinkedHashMap<>();
            clazz.put("className", classMetricsResult.getFullyQualifiedClassName());
            clazz.put("filePath", classMetricsResult.getFilepath().toString());
            clazz.put("metrics", classMetricsResult.getClassMetrics());
            classes.add(clazz);
        }
        content.put("classes", classes);
        LOGGER.debug("Content produced: {}", content);
        getResultsWriter().write(content);
    }
}
