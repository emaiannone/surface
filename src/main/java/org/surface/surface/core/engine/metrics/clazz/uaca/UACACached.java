package org.surface.surface.core.engine.metrics.clazz.uaca;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class UACACached extends UACA {
    private final UACAImpl uaca;
    private DoubleMetricValue cachedResult;

    public UACACached() {
        this.uaca = new UACAImpl();
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResult) {
        if (cachedResult == null) {
            cachedResult = uaca.compute(classResult);
        }
        return cachedResult;
    }
}
