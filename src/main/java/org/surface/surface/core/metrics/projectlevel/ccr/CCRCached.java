package org.surface.surface.core.metrics.projectlevel.ccr;

import org.surface.surface.core.metrics.projectlevel.cc.CC;
import org.surface.surface.results.ProjectAnalyzerResults;
import org.surface.surface.results.values.DoubleMetricValue;

public class CCRCached extends CCR {
    private final CCRImpl ccr;
    private DoubleMetricValue cachedResult;

    public CCRCached(CC cc) {
        this.ccr = new CCRImpl(cc);
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
