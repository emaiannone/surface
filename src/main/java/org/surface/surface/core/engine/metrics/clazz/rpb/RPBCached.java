package org.surface.surface.core.engine.metrics.clazz.rpb;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.BooleanMetricValue;

public class RPBCached extends RPB {
    private final RPBImpl rpb;
    private BooleanMetricValue cachedResult;

    public RPBCached() {
        this.rpb = new RPBImpl();
        this.cachedResult = null;
    }

    @Override
    public BooleanMetricValue compute(ClassInspectorResults classResult) {
        if (cachedResult == null) {
            cachedResult = rpb.compute(classResult);
        }
        return cachedResult;
    }
}
