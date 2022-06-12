package org.surface.surface.core.runners.results;

import org.surface.surface.core.analysis.results.AnalysisResults;

import java.util.LinkedHashMap;
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
    public Map<String, Object> export() {
        Map<String, Object> content = new LinkedHashMap<>();
        for (Map.Entry<String, AnalysisResults> analysisResultsEntry : allAnalysisResults.entrySet()) {
            String projectId = analysisResultsEntry.getKey();
            AnalysisResults analysisResults = analysisResultsEntry.getValue();
            Map<String, Object> projectExport = analysisResults.getProjectMetricsResultsAsMap();
            content.put(projectId, projectExport);
        }
        return content;
    }
}
