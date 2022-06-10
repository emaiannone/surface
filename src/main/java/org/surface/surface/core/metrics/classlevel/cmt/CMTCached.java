package org.surface.surface.core.metrics.classlevel.cmt;

import org.surface.surface.core.inspection.results.ClassInspectorResults;
import org.surface.surface.core.metrics.results.values.IntMetricValue;

public class CMTCached extends CMT {
    private final CMTImpl cmt;
    private IntMetricValue cachedResult;

    public CMTCached() {
        this.cmt = new CMTImpl();
        this.cachedResult = null;
    }

    @Override
    public IntMetricValue compute(ClassInspectorResults classResults) {
        if (cachedResult == null) {
            cachedResult = cmt.compute(classResults);
        }
        return cachedResult;
    }
}
