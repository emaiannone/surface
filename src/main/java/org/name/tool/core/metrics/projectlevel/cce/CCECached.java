package org.name.tool.core.metrics.projectlevel.cce;

import org.name.tool.core.metrics.projectlevel.cc.CC;
import org.name.tool.results.ProjectAnalyzerResults;
import org.name.tool.results.values.DoubleMetricValue;

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
