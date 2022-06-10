package org.surface.surface.core.metrics.classlevel.rpb;

import org.surface.surface.core.inspection.results.ClassInspectorResults;
import org.surface.surface.core.metrics.results.values.BooleanMetricValue;

public class RPBImpl extends RPB {

    @Override
    public BooleanMetricValue compute(ClassInspectorResults classResults) {
        boolean value = classResults.isUsingReflection();
        return new BooleanMetricValue(getName(), getCode(), value);
    }
}
