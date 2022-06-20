package org.surface.surface.core.engine.metrics.classlevel.cat;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.IntMetricValue;

public class CATCached extends CAT {
    private final CATImpl cat;
    private IntMetricValue cachedResult;

    public CATCached() {
        this.cat = new CATImpl();
        this.cachedResult = null;
    }

    @Override
    public IntMetricValue compute(ClassInspectorResults classResults) {
        if (cachedResult == null) {
            cachedResult = cat.compute(classResults);
        }
        return cachedResult;
    }
}
