package org.surface.surface.core.engine.metrics.clazz.cmai;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CMAIImpl extends CMAI {

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResult) {
        return new DoubleMetricValue(getName(), getCode(),
                computeRatio(classResult.getNumberActualMutatorAttributeInteractions(), classResult.getNumberPossibleMutatorAttributeInteractions()));
    }
}
