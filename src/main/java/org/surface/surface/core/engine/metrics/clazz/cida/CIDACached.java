package org.surface.surface.core.engine.metrics.clazz.cida;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CIDACached extends CIDA {
    private final CIDAImpl cida;
    private DoubleMetricValue cachedResult;

    public CIDACached() {
        this.cida = new CIDAImpl();
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResult) {
        if (cachedResult == null) {
            cachedResult = cida.compute(classResult);
        }
        return cachedResult;
    }

}
