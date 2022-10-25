package org.surface.surface.core.engine.metrics.clazz.cida;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CIDAImpl extends CIDA {

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        int nonPrivateNonStatic = classResults.getNumberNonPrivateInstanceClassifiedAttributes();
        int numClassifiedAttributes = classResults.getNumberClassifiedAttributes();
        double value = numClassifiedAttributes != 0 ? (double) nonPrivateNonStatic / numClassifiedAttributes : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
