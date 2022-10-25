package org.surface.surface.core.engine.metrics.clazz.caiw;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CAIWImpl extends CAIW {

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResult) {
        return new DoubleMetricValue(getName(), getCode(),
                computeRatio(classResult.getNumberActualAttributeInteractions(), classResult.getNumberPossibleAttributeInteractions()));
    }
}
