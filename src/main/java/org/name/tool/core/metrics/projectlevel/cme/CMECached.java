package org.name.tool.core.metrics.projectlevel.cme;

import org.name.tool.results.MetricResult;
import org.name.tool.results.ProjectAnalyzerResults;

public class CMECached extends CME {
    private final CMEImpl cme;
    private MetricResult<Double> cachedResult;

    public CMECached() {
        this.cme = new CMEImpl();
        this.cachedResult = null;
    }

    @Override
    public MetricResult<Double> compute(ProjectAnalyzerResults projectResults) {
        if (cachedResult == null) {
            cachedResult = cme.compute(projectResults);
        }
        return cachedResult;
    }
}
