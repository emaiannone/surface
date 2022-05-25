package org.surface.surface.core.metrics.projectlevel.ccr;

import org.surface.surface.core.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.metrics.projectlevel.cc.CC;
import org.surface.surface.core.metrics.results.values.DoubleMetricValue;

public class CCRCached extends CCR {
    private final CCRImpl ccr;
    private DoubleMetricValue cachedResult;

    public CCRCached(CC cc) {
        this.ccr = new CCRImpl(cc);
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        if (cachedResult == null) {
            cachedResult = ccr.compute(projectResults);
        }
        return cachedResult;
    }
}
