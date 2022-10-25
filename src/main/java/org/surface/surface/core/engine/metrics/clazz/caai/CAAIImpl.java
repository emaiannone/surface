package org.surface.surface.core.engine.metrics.clazz.caai;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CAAIImpl extends CAAI {

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        int actualInteractions = classResults.getNumberActualAccessorAttributeInteractions();
        int possibleInteractions = classResults.getNumberPossibleAccessorAttributeInteractions();
        double value = possibleInteractions != 0 ? (double) actualInteractions / possibleInteractions : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
