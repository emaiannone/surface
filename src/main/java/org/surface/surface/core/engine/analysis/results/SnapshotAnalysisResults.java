package org.surface.surface.core.engine.analysis.results;

import org.surface.surface.core.engine.metrics.results.ProjectMetricsResults;

import java.util.Map;

public class SnapshotAnalysisResults implements FormattableAnalysisResults {
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

    @Override
    public Map<String, Object> asMap() {
        return projectMetricsResults.toMap();
    }
}
