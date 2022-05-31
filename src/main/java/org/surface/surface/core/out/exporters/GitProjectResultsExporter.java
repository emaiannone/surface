package org.surface.surface.core.out.exporters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;
import org.surface.surface.core.out.writers.FileWriter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class GitProjectResultsExporter extends ResultsExporter<Map<String, ProjectMetricsResults>> {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final String PROJECT_NAME = "projectName";
    private static final String LOCAL_PATH = "localPath";
    private static final String REMOTE_PATH = "remotePath";
    private static final String NAME = "name";
    private static final String PATH = "path";
    private static final String REVISIONS = "revisions";

    private final String remotePath;

    public GitProjectResultsExporter(String remotePath) {
        this.remotePath = remotePath;
    }

    @Override
    public void export(Map<String, ProjectMetricsResults> projectMetricsResults, FileWriter writer) throws IOException {
        Map<String, Object> content = new LinkedHashMap<>();
        Map<String, Object> revisions = new LinkedHashMap<>();
        Map.Entry<String, ProjectMetricsResults> first = projectMetricsResults.entrySet().iterator().next();
        content.put(PROJECT_NAME, first.getValue().getProjectName());
        content.put(LOCAL_PATH, first.getValue().getProjectPath().toString());
        if (remotePath != null) {
            content.put(REMOTE_PATH, remotePath);
        }
        for (Map.Entry<String, ProjectMetricsResults> entry : projectMetricsResults.entrySet()) {
            Map<String, Object> projectResults = entry.getValue().toMap();
            projectResults.remove(NAME);
            projectResults.remove(PATH);
            revisions.put(entry.getKey(), projectResults);
        }
        content.put(REVISIONS, revisions);
        LOGGER.trace("Results exported: {}", content);
        writer.write(content);
    }
}
