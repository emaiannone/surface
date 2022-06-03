package org.surface.surface.core.analysis;

import org.surface.surface.core.metrics.api.MetricsManager;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;

import java.util.Map;

public abstract class Analyzer {
    private final String filesRegex;
    private final MetricsManager metricsManager;

    public Analyzer(String filesRegex, MetricsManager metricsManager) {
        this.filesRegex = filesRegex;
        this.metricsManager = metricsManager;
    }

    public String getFilesRegex() {
        return filesRegex;
    }

    public MetricsManager getMetricsManager() {
        return metricsManager;
    }

    public abstract Map<String, ProjectMetricsResults> analyze() throws Exception;
}
