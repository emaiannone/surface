package org.surface.surface.core.configuration.runners.results;

import org.surface.surface.core.engine.analysis.results.SnapshotAnalysisResults;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LocalDirectoryRunResults extends RunResults {
    private SnapshotAnalysisResults analysisResults;

    public LocalDirectoryRunResults() {
    }

    public void setAnalysisResults(SnapshotAnalysisResults analysisResults) {
        this.analysisResults = analysisResults;
    }

    public List<Map<String, Object>> export() {
        List<Map<String, Object>> exportList = new ArrayList<>();
        exportList.add(analysisResults.getProjectMetricsResultsAsMap());
        return exportList;
    }
}
