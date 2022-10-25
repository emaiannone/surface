package org.surface.surface.core.engine.metrics.clazz.cida;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CIDAImpl extends CIDA {

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResult) {
        return new DoubleMetricValue(getName(), getCode(),
                computeRatio(classResult.getNumberNonPrivateInstanceClassifiedAttributes(), classResult.getNumberClassifiedAttributes()));
    }
}
