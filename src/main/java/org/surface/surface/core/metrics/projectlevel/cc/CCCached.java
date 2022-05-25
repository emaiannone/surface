package org.surface.surface.core.metrics.projectlevel.cc;

import org.surface.surface.core.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.metrics.results.values.IntMetricValue;

public class CCCached extends CC {
    private final CCImpl cc;
    private IntMetricValue cachedResult;

    public CCCached() {
        this.cc = new CCImpl();
        this.cachedResult = null;
    }

    @Override
    public IntMetricValue compute(ProjectInspectorResults projectResults) {
        if (cachedResult == null) {
            cachedResult = cc.compute(projectResults);
        }
        return cachedResult;
    }
}
