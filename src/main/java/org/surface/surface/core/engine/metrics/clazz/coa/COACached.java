package org.surface.surface.core.engine.metrics.clazz.coa;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class COACached extends COA {
    private final COAImpl coa;
    private DoubleMetricValue cachedResult;

    public COACached() {
        this.coa = new COAImpl();
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResult) {
        if (cachedResult == null) {
            cachedResult = coa.compute(classResult);
        }
        return cachedResult;
    }
}
