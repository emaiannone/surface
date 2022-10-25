package org.surface.surface.core.engine.metrics.clazz.cmt;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.IntMetricValue;

public class CMTCached extends CMT {
    private final CMTImpl cmt;
    private IntMetricValue cachedResult;

    public CMTCached() {
        this.cmt = new CMTImpl();
        this.cachedResult = null;
    }

    @Override
    public IntMetricValue compute(ClassInspectorResults classResult) {
        if (cachedResult == null) {
            cachedResult = cmt.compute(classResult);
        }
        return cachedResult;
    }
}
