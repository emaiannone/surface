package org.surface.surface.core.analysis.results;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HistoryAnalysisResults implements AnalysisResults {
    private final Map<String, SnapshotAnalysisResults> allSnapshotAnalysisResults;

    private static final String PROJECT_NAME = "projectName";
    private static final String LOCAL_PATH = "localPath";
    private static final String REVISIONS = "revisions";
    private static final String METRICS = "metrics";
    private static final String CLASSES = "classes";
    private static final String COMMIT_HASH = "commitHash";

    public HistoryAnalysisResults() {
        this.allSnapshotAnalysisResults = new LinkedHashMap<>();
    }

    public void addSnapshotAnalysisResults(String commitId, SnapshotAnalysisResults snapshotAnalysisResults) {
        allSnapshotAnalysisResults.put(commitId, snapshotAnalysisResults);
    }

    public String getProjectPath() {
        return getFirstSnapshotAnalysisResults().getProjectDirPath();
    }

    public String getProjectName() {
        return getFirstSnapshotAnalysisResults().getProjectName();
    }

    private SnapshotAnalysisResults getFirstSnapshotAnalysisResults() {
        return allSnapshotAnalysisResults.entrySet().iterator().next().getValue();
    }

    public Map<String, SnapshotAnalysisResults> getResults() {
        return new LinkedHashMap<>(allSnapshotAnalysisResults);
    }

    public Map<String, Object> getProjectMetricsResultsAsMap() {
        Map<String, Object> content = new LinkedHashMap<>();
        List<Object> revisions = new ArrayList<>();
        content.put(PROJECT_NAME, getProjectName());
        content.put(LOCAL_PATH, getProjectPath());
        for (Map.Entry<String, SnapshotAnalysisResults> entry : allSnapshotAnalysisResults.entrySet()) {
            Map<String, Object> projectResults = entry.getValue().getProjectMetricsResultsAsMap();
            Object metrics = projectResults.remove(METRICS);
            Object classes = projectResults.remove(CLASSES);
            projectResults.remove(PROJECT_NAME);
            projectResults.remove(LOCAL_PATH);
            projectResults.put(COMMIT_HASH, entry.getKey());
            projectResults.put(METRICS, metrics);
            projectResults.put(CLASSES, classes);
            revisions.add(projectResults);
        }
        content.put(REVISIONS, revisions);
        return content;
    }

    public void addAll(HistoryAnalysisResults analysisResults) {
        allSnapshotAnalysisResults.putAll(analysisResults.getResults());
    }

    public void removeProjectResult(String project) {
        allSnapshotAnalysisResults.remove(project);
    }
}
