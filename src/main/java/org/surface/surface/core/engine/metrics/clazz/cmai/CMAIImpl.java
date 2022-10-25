package org.surface.surface.core.engine.metrics.clazz.cmai;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CMAIImpl extends CMAI {

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        int actualInteractions = classResults.getNumberActualMutatorAttributeInteractions();
        int possibleInteractions = classResults.getNumberPossibleMutatorAttributeInteractions();
        double value = possibleInteractions != 0 ? (double) actualInteractions / possibleInteractions : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
