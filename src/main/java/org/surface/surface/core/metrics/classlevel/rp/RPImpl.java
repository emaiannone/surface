package org.surface.surface.core.metrics.classlevel.rp;

import org.surface.surface.core.inspection.results.ClassInspectorResults;
import org.surface.surface.core.metrics.results.values.BooleanMetricValue;

public class RPImpl extends RP {

    @Override
    public BooleanMetricValue compute(ClassInspectorResults classResults) {
        boolean value = classResults.isUsingReflection();
        return new BooleanMetricValue(getName(), getCode(), value);
    }
}
