package org.surface.surface.core.metrics.classlevel.ca;

import org.surface.surface.core.inspection.results.ClassInspectorResults;
import org.surface.surface.core.metrics.results.values.IntMetricValue;

public class CACached extends CA {
    private final CAImpl classifiedAttributes;
    private IntMetricValue cachedResult;

    public CACached() {
        this.classifiedAttributes = new CAImpl();
        this.cachedResult = null;
    }

    @Override
    public IntMetricValue compute(ClassInspectorResults classResults) {
        if (cachedResult == null) {
            cachedResult = classifiedAttributes.compute(classResults);
        }
        return cachedResult;
    }
}
