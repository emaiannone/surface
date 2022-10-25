package org.surface.surface.core.engine.metrics.classlevel.cmai;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CMAICached extends CMAI {
    private final CMAIImpl cmai;
    private DoubleMetricValue cachedResult;

    public CMAICached() {
        this.cmai = new CMAIImpl();
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        if (cachedResult == null) {
            cachedResult = cmai.compute(classResults);
        }
        return cachedResult;
    }
}
