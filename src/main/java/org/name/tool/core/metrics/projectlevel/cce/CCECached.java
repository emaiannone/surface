package org.name.tool.core.metrics.projectlevel.cce;

import org.name.tool.core.metrics.projectlevel.cc.CC;
import org.name.tool.core.results.ProjectAnalyzerResults;
import org.name.tool.core.results.SecurityMetricResult;

public class CCECached extends CCE {
    private final CCEImpl cce;
    private SecurityMetricResult<Double> cachedResult;

    public CCECached(CC cc) {
        this.cce = new CCEImpl(cc);
        this.cachedResult = null;
    }

    @Override
    public SecurityMetricResult<Double> compute(ProjectAnalyzerResults projectResults) {
        if (cachedResult == null) {
            cachedResult = cce.compute(projectResults);
        }
        return cachedResult;
    }
}
