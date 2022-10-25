package org.surface.surface.core.engine.metrics.classlevel.cida;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CIDAImpl extends CIDA {

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        int nonPrivateNonStatic = classResults.getNumberNonPrivateInstanceClassifiedAttributes();
        int catValue = classResults.getNumberClassifiedAttributes();
        double value = catValue != 0.0 ? (double) nonPrivateNonStatic / catValue : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
