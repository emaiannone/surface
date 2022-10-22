package org.surface.surface.core.engine.analysis.results;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HistoryAnalysisResults implements FormattableAnalysisResults {
    private final String repoLocation;
    private final Map<String, SnapshotAnalysisResults> allSnapshotAnalysisResults;

    public static final String PROJECT_NAME = "projectName";
    public static final String LOCATION = "location";
    public static final String WORKING_DIR = "workingDir";
    public static final String REVISIONS = "revisions";
    public static final String METRICS = "projectMetrics";
    public static final String CRITICAL_CLASSES = "criticalClasses";
    public static final String REVISION = "revision";

    public HistoryAnalysisResults(String repoLocation) {
        this.repoLocation = repoLocation;
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

    @Override
    public Map<String, Object> asMap() {
        Map<String, Object> content = new LinkedHashMap<>();
        List<Object> revisions = new ArrayList<>();
        content.put(PROJECT_NAME, getProjectName());
        content.put(LOCATION, repoLocation);
        content.put(WORKING_DIR, getProjectPath());
        for (Map.Entry<String, SnapshotAnalysisResults> entry : allSnapshotAnalysisResults.entrySet()) {
            Map<String, Object> projectResults = entry.getValue().asMap();
            Object metrics = projectResults.remove(METRICS);
            Object classes = projectResults.remove(CRITICAL_CLASSES);
            projectResults.remove(PROJECT_NAME);
            projectResults.remove(WORKING_DIR);
            projectResults.put(REVISION, entry.getKey());
            projectResults.put(METRICS, metrics);
            projectResults.put(CRITICAL_CLASSES, classes);
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
