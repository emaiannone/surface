package org.surface.surface.core.analysis;

import org.surface.surface.core.metrics.results.ProjectMetricsResults;

import java.util.LinkedHashMap;
import java.util.Map;

public class AnalysisResults {
    private final Map<String, ProjectMetricsResults> results;

    public AnalysisResults() {
        this.results = new LinkedHashMap<>();
    }

    public void addProjectResult(String project, ProjectMetricsResults result) {
        results.put(project, result);
    }

    public ProjectMetricsResults getFirstProjectResult() {
        return results.entrySet().iterator().next().getValue();
    }

    public void addAll(AnalysisResults analysisResults) {
        results.putAll(analysisResults.getResults());
    }

    public void removeProjectResult(String project) {
        results.remove(project);
    }

    public Map<String, ProjectMetricsResults> getResults() {
        return new LinkedHashMap<>(results);
    }
}
