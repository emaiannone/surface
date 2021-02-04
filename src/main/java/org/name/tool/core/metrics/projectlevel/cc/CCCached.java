package org.name.tool.core.metrics.projectlevel.cc;

import org.name.tool.results.MetricValue;
import org.name.tool.results.ProjectAnalyzerResults;

public class CCCached extends CC {
    private final CCImpl cc;
    private MetricValue<Integer> cachedResult;

    public CCCached() {
        this.cc = new CCImpl();
        this.cachedResult = null;
    }

    @Override
    public MetricValue<Integer> compute(ProjectAnalyzerResults projectResults) {
        if (cachedResult == null) {
            cachedResult = cc.compute(projectResults);
        }
        return cachedResult;
    }
}
