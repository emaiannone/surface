package org.surface.surface.core.configuration.runners.results;

import org.surface.surface.core.engine.analysis.results.HistoryAnalysisResults;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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

    public List<Map<String, Object>> export() {
        List<Map<String, Object>> exportList = new ArrayList<>();
        Map<String, Object> content = new LinkedHashMap<>();
        content.put(LOCATION, repoLocation);
        content.putAll(analysisResults.getProjectMetricsResultsAsMap());
        exportList.add(content);
        return exportList;
    }

}
