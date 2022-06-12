package org.surface.surface.core.runners.results;

import org.surface.surface.core.analysis.results.HistoryAnalysisResults;

import java.util.LinkedHashMap;
import java.util.Map;

public class GitRunResults extends RunResults {
    private HistoryAnalysisResults analysisResults;
    private final String repoLocation;

    private static final String LOCATION = "location";

    public GitRunResults(String repoLocation) {
        this.repoLocation = repoLocation;
    }

    public HistoryAnalysisResults getAnalysisResults() {
        return analysisResults;
    }

    public void setAnalysisResults(HistoryAnalysisResults analysisResults) {
        this.analysisResults = analysisResults;
    }

    public Map<String, Object> export() {
        Map<String, Object> content = new LinkedHashMap<>();
        content.put(LOCATION, repoLocation);
        content.putAll(analysisResults.getProjectMetricsResultsAsMap());
        return content;
    }

}
