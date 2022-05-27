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

    public static final String PROJECT_NAME = "projectName";
    public static final String LOCAL_PATH = "localPath";
    public static final String REMOTE_PATH = "remotePath";
    public static final String NAME = "name";
    public static final String PATH = "path";
    public static final String REVISIONS = "revisions";

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
        getResultsWriter().write(content);
    }
}
