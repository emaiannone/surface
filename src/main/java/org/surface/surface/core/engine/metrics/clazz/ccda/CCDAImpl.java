package org.surface.surface.core.engine.metrics.clazz.ccda;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CCDAImpl extends CCDA {

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        int nonPrivateStatic = classResults.getNumberNonPrivateClassClassifiedAttributes();
        int numClassifiedAttributes = classResults.getNumberClassifiedAttributes();
        double value = numClassifiedAttributes != 0 ? (double) nonPrivateStatic / numClassifiedAttributes : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
