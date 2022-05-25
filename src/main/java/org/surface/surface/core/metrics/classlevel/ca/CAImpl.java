package org.surface.surface.core.metrics.classlevel.ca;

import org.surface.surface.core.inspection.results.ClassInspectorResults;
import org.surface.surface.core.metrics.results.values.IntMetricValue;

public class CAImpl extends CA {

    @Override
    public IntMetricValue compute(ClassInspectorResults classResults) {
        int value = classResults.getClassifiedAttributes().size();
        return new IntMetricValue(getName(), getCode(), value);
    }
}
