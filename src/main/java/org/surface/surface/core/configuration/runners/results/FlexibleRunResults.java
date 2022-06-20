package org.surface.surface.core.configuration.runners.results;

import org.surface.surface.core.engine.analysis.results.AnalysisResults;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FlexibleRunResults extends RunResults {
    private final Map<String, AnalysisResults> allAnalysisResults;

    public FlexibleRunResults() {
        this.allAnalysisResults = new LinkedHashMap<>();
    }

    public void addAnalysisResults(String project, AnalysisResults results) {
        allAnalysisResults.put(project, results);
    }

    public void addAll(FlexibleRunResults flexibleRunResults) {
        this.allAnalysisResults.putAll(flexibleRunResults.getResults());
    }

    public void removeProjectResult(String project) {
        allAnalysisResults.remove(project);
    }

    public boolean containsKey(String key) {
        return allAnalysisResults.containsKey(key);
    }

    public Map<String, AnalysisResults> getResults() {
        return new LinkedHashMap<>(allAnalysisResults);
    }

    @Override
    public List<Map<String, Object>> export() {
        List<Map<String, Object>> exportList = new ArrayList<>();
        for (Map.Entry<String, AnalysisResults> analysisResultsEntry : allAnalysisResults.entrySet()) {
            AnalysisResults analysisResults = analysisResultsEntry.getValue();
            Map<String, Object> projectExport = analysisResults.getProjectMetricsResultsAsMap();
            exportList.add(projectExport);
        }
        return exportList;
    }
}
