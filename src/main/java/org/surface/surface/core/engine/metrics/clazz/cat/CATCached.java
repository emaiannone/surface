package org.surface.surface.core.engine.metrics.clazz.cat;

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
    public IntMetricValue compute(ClassInspectorResults classResult) {
        if (cachedResult == null) {
            cachedResult = cat.compute(classResult);
        }
        return cachedResult;
    }
}
