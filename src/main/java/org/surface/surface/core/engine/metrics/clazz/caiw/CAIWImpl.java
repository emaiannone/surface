package org.surface.surface.core.engine.metrics.clazz.caiw;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CAIWImpl extends CAIW {

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        int actualInteractions = classResults.getNumberActualAttributeInteractions();
        int possibleInteractions = classResults.getNumberPossibleAttributeInteractions();
        double value = possibleInteractions != 0 ? (double) actualInteractions / possibleInteractions : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
