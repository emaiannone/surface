package org.name.tool.core.metrics.projectlevel.cme;

import org.name.tool.results.MetricValue;
import org.name.tool.results.ProjectAnalyzerResults;

public class CMECached extends CME {
    private final CMEImpl cme;
    private MetricValue<Double> cachedResult;

    public CMECached() {
        this.cme = new CMEImpl();
        this.cachedResult = null;
    }

    @Override
    public MetricValue<Double> compute(ProjectAnalyzerResults projectResults) {
        if (cachedResult == null) {
            cachedResult = cme.compute(projectResults);
        }
        return cachedResult;
    }
}
