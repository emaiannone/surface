package org.surface.surface.core.configuration.runners.results;

import org.surface.surface.core.engine.analysis.results.FormattableAnalysisResults;

import java.util.LinkedHashMap;
import java.util.Map;

public class RunResults {
    private final Map<String, FormattableAnalysisResults> allAnalysisResults;

    public RunResults() {
        this.allAnalysisResults = new LinkedHashMap<>();
    }

    public void addAnalysisResults(String project, FormattableAnalysisResults results) {
        allAnalysisResults.put(project, results);
    }

    public void addAll(RunResults other) {
        this.allAnalysisResults.putAll(other.getAllResults());
    }

    public void removeProjectResult(String project) {
        allAnalysisResults.remove(project);
    }

    public boolean containsKey(String key) {
        return allAnalysisResults.containsKey(key);
    }

    public int getNumberResults() {
        return allAnalysisResults.size();
    }

    public Map<String, FormattableAnalysisResults> getAllResults() {
        return new LinkedHashMap<>(allAnalysisResults);
    }

    public FormattableAnalysisResults getFirstResult() {
        return allAnalysisResults.entrySet().iterator().next().getValue();
    }
}
