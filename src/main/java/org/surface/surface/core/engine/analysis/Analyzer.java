package org.surface.surface.core.engine.analysis;

import org.surface.surface.core.engine.analysis.results.AnalysisResults;
import org.surface.surface.core.engine.metrics.api.MetricsManager;

public abstract class Analyzer {
    private final String filesRegex;
    private final MetricsManager metricsManager;
    private final boolean includeTests;

    public Analyzer(String filesRegex, MetricsManager metricsManager, boolean includeTests) {
        this.filesRegex = filesRegex;
        this.metricsManager = metricsManager;
        this.includeTests = includeTests;
    }

    public String getFilesRegex() {
        return filesRegex;
    }

    public MetricsManager getMetricsManager() {
        return metricsManager;
    }

    public boolean isIncludeTests() {
        return includeTests;
    }

    public abstract AnalysisResults analyze() throws Exception;
}
