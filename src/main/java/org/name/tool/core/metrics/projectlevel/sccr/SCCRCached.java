package org.name.tool.core.metrics.projectlevel.sccr;

import org.name.tool.core.metrics.projectlevel.cc.CC;
import org.name.tool.results.MetricValue;
import org.name.tool.results.ProjectAnalyzerResults;

public class SCCRCached extends SCCR {
    private final SCCRImpl ccr;
    private MetricValue<Double> cachedResult;

    public SCCRCached(CC cc) {
        this.ccr = new SCCRImpl(cc);
        this.cachedResult = null;
    }

    @Override
    public MetricValue<Double> compute(ProjectAnalyzerResults projectResults) {
        if (cachedResult == null) {
            cachedResult = ccr.compute(projectResults);
        }
        return cachedResult;
    }
}
