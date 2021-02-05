package org.surface.surface.core.metrics.projectlevel.cce;

import org.surface.surface.core.metrics.projectlevel.cc.CC;
import org.surface.surface.results.ProjectAnalyzerResults;
import org.surface.surface.results.values.DoubleMetricValue;

public class CCECached extends CCE {
    private final CCEImpl cce;
    private DoubleMetricValue cachedResult;

    public CCECached(CC cc) {
        this.cce = new CCEImpl(cc);
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectAnalyzerResults projectResults) {
        if (cachedResult == null) {
            cachedResult = cce.compute(projectResults);
        }
        return cachedResult;
    }
}
