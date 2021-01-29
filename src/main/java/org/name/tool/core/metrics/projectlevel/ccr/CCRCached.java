package org.name.tool.core.metrics.projectlevel.ccr;

import org.name.tool.core.metrics.projectlevel.cc.CC;
import org.name.tool.core.results.ProjectAnalyzerResults;
import org.name.tool.core.results.SecurityMetricResult;

public class CCRCached extends CCR {
    private final CCRImpl ccr;
    private SecurityMetricResult<Double> cachedResult;

    public CCRCached(CC cc) {
        this.ccr = new CCRImpl(cc);
        this.cachedResult = null;
    }

    @Override
    public SecurityMetricResult<Double> compute(ProjectAnalyzerResults projectResults) {
        if (cachedResult == null) {
            cachedResult = ccr.compute(projectResults);
        }
        return cachedResult;
    }
}
