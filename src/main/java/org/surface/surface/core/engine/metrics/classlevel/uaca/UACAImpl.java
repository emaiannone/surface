package org.surface.surface.core.engine.metrics.classlevel.uaca;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;


public class UACAImpl extends UACA {
    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        int numUnaccessedAssignedAttrs = classResults.getNumberUnaccessedAssignedAttributes();
        int catValue = classResults.getNumberClassifiedAttributes();
        double value = catValue != 0.0 ? (double) numUnaccessedAssignedAttrs / catValue : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
