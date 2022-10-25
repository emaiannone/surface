package org.surface.surface.core.engine.metrics.clazz.ccda;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CCDAImpl extends CCDA {

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResult) {
        return new DoubleMetricValue(getName(), getCode(),
                computeRatio(classResult.getNumberNonPrivateClassClassifiedAttributes(), classResult.getNumberClassifiedAttributes()));
    }
}
