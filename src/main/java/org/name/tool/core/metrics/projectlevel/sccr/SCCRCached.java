package org.name.tool.core.metrics.projectlevel.sccr;

import org.name.tool.core.metrics.projectlevel.cc.CC;
import org.name.tool.core.results.MetricResult;
import org.name.tool.core.results.ProjectAnalyzerResults;

public class SCCRCached extends SCCR {
    private final SCCRImpl ccr;
    private MetricResult<Double> cachedResult;

    public SCCRCached(CC cc) {
        this.ccr = new SCCRImpl(cc);
        this.cachedResult = null;
    }

    @Override
    public MetricResult<Double> compute(ProjectAnalyzerResults projectResults) {
        if (cachedResult == null) {
            cachedResult = ccr.compute(projectResults);
        }
        return cachedResult;
    }
}
