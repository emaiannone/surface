package org.surface.surface.core.runners.results;

import org.surface.surface.core.analysis.results.SnapshotAnalysisResults;

import java.util.Map;

public class LocalDirectoryRunResults extends RunResults {
    private SnapshotAnalysisResults analysisResults;

    public LocalDirectoryRunResults() {
    }

    public void setAnalysisResults(SnapshotAnalysisResults analysisResults) {
        this.analysisResults = analysisResults;
    }

    public Map<String, Object> export() {
        return analysisResults.getProjectMetricsResultsAsMap();
    }
}
