package org.name.tool.core.metrics.projectlevel.cc;

import org.name.tool.core.results.MetricResult;
import org.name.tool.core.results.ProjectAnalyzerResults;

public class CCCached extends CC {
    private final CCImpl cc;
    private MetricResult<Integer> cachedResult;

    public CCCached() {
        this.cc = new CCImpl();
        this.cachedResult = null;
    }

    @Override
    public MetricResult<Integer> compute(ProjectAnalyzerResults projectResults) {
        if (cachedResult == null) {
            cachedResult = cc.compute(projectResults);
        }
        return cachedResult;
    }
}
