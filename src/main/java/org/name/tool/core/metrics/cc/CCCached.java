package org.name.tool.core.metrics.cc;

import org.name.tool.core.results.ProjectAnalyzerResults;
import org.name.tool.core.results.SecurityMetricResult;

public class CCCached extends CC {
    private final CCImpl cc;
    private SecurityMetricResult<Integer> cachedResult;

    public CCCached() {
        this.cc = new CCImpl();
        this.cachedResult = null;
    }

    @Override
    public SecurityMetricResult<Integer> compute(ProjectAnalyzerResults projectResults) {
        if (cachedResult == null) {
            cachedResult = cc.compute(projectResults);
        }
        return cachedResult;
    }
}
