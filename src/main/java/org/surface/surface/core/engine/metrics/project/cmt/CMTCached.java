package org.surface.surface.core.engine.metrics.project.cmt;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.IntMetricValue;

public class CMTCached extends CMT {
    private final CMTImpl cmt;
    private IntMetricValue cachedResult;

    public CMTCached() {
        this.cmt = new CMTImpl();
        this.cachedResult = null;
    }

    @Override
    public IntMetricValue compute(ProjectInspectorResults projectResult) {
        if (cachedResult == null) {
            cachedResult = cmt.compute(projectResult);
        }
        return cachedResult;
    }
}
