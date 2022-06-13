package org.surface.surface.core.engine.metrics.projectlevel.cct;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.IntMetricValue;

public class CCTCached extends CCT {
    private final CCTImpl cct;
    private IntMetricValue cachedResult;

    public CCTCached() {
        this.cct = new CCTImpl();
        this.cachedResult = null;
    }

    @Override
    public IntMetricValue compute(ProjectInspectorResults projectResults) {
        if (cachedResult == null) {
            cachedResult = cct.compute(projectResults);
        }
        return cachedResult;
    }
}
