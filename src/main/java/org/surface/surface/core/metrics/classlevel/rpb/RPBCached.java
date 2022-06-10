package org.surface.surface.core.metrics.classlevel.rpb;

import org.surface.surface.core.inspection.results.ClassInspectorResults;
import org.surface.surface.core.metrics.results.values.BooleanMetricValue;

public class RPBCached extends RPB {
    private final RPBImpl rpb;
    private BooleanMetricValue cachedResult;

    public RPBCached() {
        this.rpb = new RPBImpl();
        this.cachedResult = null;
    }

    @Override
    public BooleanMetricValue compute(ClassInspectorResults classResults) {
        if (cachedResult == null) {
            cachedResult = rpb.compute(classResults);
        }
        return cachedResult;
    }
}
