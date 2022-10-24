package org.surface.surface.core.engine.analysis.results;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HistoryAnalysisResults implements FormattableAnalysisResults {
    private static final String PROJECT_NAME = "projectName";
    private static final String LOCATION = "location";
    private static final String PROJECT_DIR = "projectDir";
    private static final String REVISIONS = "revisions";
    private static final String METRICS = "projectMetrics";
    private static final String CRITICAL_CLASSES = "criticalClasses";
    private static final String REVISION = "revision";
    private final String repoLocation;
    private final Map<String, SnapshotAnalysisResults> allSnapshotAnalysisResults;

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

    public Map<String, SnapshotAnalysisResults> getResults() {
        return new LinkedHashMap<>(allSnapshotAnalysisResults);
    }

    @Override
    public Map<String, Object> asMap() {
        Map<String, Object> content = new LinkedHashMap<>();
        content.put(PROJECT_NAME, getProjectName());
        content.put(LOCATION, repoLocation);
        content.put(PROJECT_DIR, getProjectPath());
        content.put(REVISIONS, getRevisionsAsList());
        return content;
    }

    @Override
    public String asPlain() {
        return "Project: " + getProjectName() + "\n" +
                "Location: " + repoLocation + "\n" +
                "Project Directory: " + getProjectPath() + "\n" +
                "Revisions:\n\t" + getRevisionsAsPlain().replace("\n", "\n\t");
    }

    public void addAll(HistoryAnalysisResults analysisResults) {
        allSnapshotAnalysisResults.putAll(analysisResults.getResults());
    }

    public void removeProjectResult(String project) {
        allSnapshotAnalysisResults.remove(project);
    }

    private SnapshotAnalysisResults getFirstSnapshotAnalysisResults() {
        return allSnapshotAnalysisResults.entrySet().iterator().next().getValue();
    }

    private List<Map<String, Object>> getRevisionsAsList() {
        List<Map<String, Object>> revisions = new ArrayList<>();
        for (Map.Entry<String, SnapshotAnalysisResults> entry : allSnapshotAnalysisResults.entrySet()) {
            Map<String, Object> projectResults = entry.getValue().getProjectMetricsResults().toMap();
            Object metrics = projectResults.remove(METRICS);
            Object classes = projectResults.remove(CRITICAL_CLASSES);
            projectResults.remove(PROJECT_NAME);
            projectResults.remove(PROJECT_DIR);
            projectResults.put(REVISION, entry.getKey());
            projectResults.put(METRICS, metrics);
            projectResults.put(CRITICAL_CLASSES, classes);
            revisions.add(projectResults);
        }
        return revisions;
    }

    private String getRevisionsAsPlain() {
        List<String> revisionsStrings = new ArrayList<>();
        for (Map.Entry<String, SnapshotAnalysisResults> entry : allSnapshotAnalysisResults.entrySet()) {
            SnapshotAnalysisResults res = entry.getValue();
            String classesNames = res.getProjectMetricsResults().getCriticalClassesAsPlain();
            String revisionString = "Revision: " + entry.getKey() + "\n" +
                    "Project Metrics: " + res.getProjectMetricsResults().getMetricsAsPlain() + "\n" +
                    "Critical Classes: " + (res.getProjectMetricsResults().getNumberCriticalClasses() > 0 ? "\n\t" + classesNames.replace("\n", "\n\t") : classesNames);
            revisionsStrings.add(revisionString);
        }
        return String.join("\n\n", revisionsStrings);
    }
}
