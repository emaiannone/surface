package org.surface.surface.core.engine.metrics.clazz.uaca;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class UACAImpl extends UACA {

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResult) {
        return new DoubleMetricValue(getName(), getCode(),
                computeRatio(classResult.getNumberUnaccessedAssignedAttributes(), classResult.getNumberClassifiedAttributes()));
    }
}
