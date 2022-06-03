package org.surface.surface.core.out.exporters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;

import java.util.LinkedHashMap;
import java.util.Map;

public class GitProjectResultsExporter extends ResultsExporter<Map<String, ProjectMetricsResults>> {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final String PROJECT_NAME = "projectName";
    private static final String LOCAL_PATH = "localPath";
    private static final String REMOTE_PATH = "remotePath";
    private static final String REVISIONS = "revisions";

    private final String remotePath;

    public GitProjectResultsExporter(String remotePath) {
        this.remotePath = remotePath;
    }
    @Override
    public Map<String, Object> export(Map<String, ProjectMetricsResults> projectMetricsResults) {
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
            projectResults.remove(PROJECT_NAME);
            projectResults.remove(LOCAL_PATH);
            revisions.put(entry.getKey(), projectResults);
        }
        content.put(REVISIONS, revisions);
        return content;
    }
}
