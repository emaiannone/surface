package org.surface.surface.core.engine.metrics.classlevel.uaca;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;


public class UACAImpl extends UACA {
    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        long numUnaccessedAssignedAttrs = classResults.getClassifiedAttributes()
                .stream()
                .filter(ca -> classResults.isMutated(ca) && !classResults.isAccessed(ca))
                .count();
        int catValue = classResults.getNumberClassifiedAttributes();
        double value = catValue != 0.0 ? (double) numUnaccessedAssignedAttrs / catValue : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
