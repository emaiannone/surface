package org.surface.surface.core.analysis.results;

import org.surface.surface.core.metrics.results.ProjectMetricsResults;

import java.util.Map;

public class SnapshotAnalysisResults implements AnalysisResults {
    private final String projectDirPath;
    private final ProjectMetricsResults projectMetricsResults;

    public SnapshotAnalysisResults(String projectDirPath, ProjectMetricsResults projectMetricsResults) {
        this.projectDirPath = projectDirPath;
        this.projectMetricsResults = projectMetricsResults;
    }

    public String getProjectDirPath() {
        return projectDirPath;
    }

    public ProjectMetricsResults getProjectMetricsResults() {
        return projectMetricsResults;
    }

    public String getProjectName() {
        return projectMetricsResults.getProjectName();
    }

    public Map<String, Object> getProjectMetricsResultsAsMap() {
        return projectMetricsResults.toMap();
    }
}
