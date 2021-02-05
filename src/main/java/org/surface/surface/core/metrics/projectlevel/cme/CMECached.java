package org.surface.surface.core.metrics.projectlevel.cme;

import org.surface.surface.results.ProjectAnalyzerResults;
import org.surface.surface.results.values.DoubleMetricValue;

public class CMECached extends CME {
    private final CMEImpl cme;
    private DoubleMetricValue cachedResult;

    public CMECached() {
        this.cme = new CMEImpl();
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectAnalyzerResults projectResults) {
        if (cachedResult == null) {
            cachedResult = cme.compute(projectResults);
        }
        return cachedResult;
    }
}
