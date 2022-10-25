package org.surface.surface.core.engine.metrics.clazz.caiw;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CAIWCached extends CAIW {
    private final CAIWImpl caiw;
    private DoubleMetricValue cachedResult;

    public CAIWCached() {
        this.caiw = new CAIWImpl();
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResult) {
        if (cachedResult == null) {
            cachedResult = caiw.compute(classResult);
        }
        return cachedResult;
    }
}
