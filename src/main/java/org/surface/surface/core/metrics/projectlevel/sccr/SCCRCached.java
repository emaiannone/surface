package org.surface.surface.core.metrics.projectlevel.sccr;

import org.surface.surface.core.metrics.projectlevel.cc.CC;
import org.surface.surface.results.ProjectAnalyzerResults;
import org.surface.surface.results.values.DoubleMetricValue;

public class SCCRCached extends SCCR {
    private final SCCRImpl ccr;
    private DoubleMetricValue cachedResult;

    public SCCRCached(CC cc) {
        this.ccr = new SCCRImpl(cc);
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectAnalyzerResults projectResults) {
        if (cachedResult == null) {
            cachedResult = ccr.compute(projectResults);
        }
        return cachedResult;
    }
}
