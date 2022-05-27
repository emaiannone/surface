package org.surface.surface.out.exporters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;
import org.surface.surface.out.writers.Writer;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class GitProjectResultsExporter extends ResultsExporter<Map<String, ProjectMetricsResults>> {
    private static final Logger LOGGER = LogManager.getLogger();

    private final String remotePath;

    public GitProjectResultsExporter(Writer writer, String remotePath) {
        super(writer);
        this.remotePath = remotePath;
    }

    @Override
    public void export(Map<String, ProjectMetricsResults> projectMetricsResults) throws IOException {
        Map<String, Object> content = new LinkedHashMap<>();
        Map<String, Object> revisions = new LinkedHashMap<>();
        Map.Entry<String, ProjectMetricsResults> first = projectMetricsResults.entrySet().iterator().next();
        content.put("projectName", first.getValue().getProjectName());
        content.put("localPath", first.getValue().getProjectPath().toString());
        if (remotePath != null) {
            content.put("remotePath", remotePath);
        }
        for (Map.Entry<String, ProjectMetricsResults> entry : projectMetricsResults.entrySet()) {
            Map<String, Object> projectResults = entry.getValue().toMap();
            projectResults.remove("name");
            projectResults.remove("path");
            revisions.put(entry.getKey(), projectResults);
        }
        content.put("revisions", revisions);
        LOGGER.trace("Results exported: {}", content);
        getResultsWriter().write(content);
    }
}
