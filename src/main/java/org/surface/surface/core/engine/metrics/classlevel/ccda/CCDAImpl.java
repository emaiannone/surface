package org.surface.surface.core.engine.metrics.classlevel.ccda;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CCDAImpl extends CCDA {

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        int nonPrivateStatic = classResults.getNumberNonPrivateClassClassifiedAttributes();
        int catValue = classResults.getNumberClassifiedAttributes();
        double value = catValue != 0.0 ? (double) nonPrivateStatic / catValue : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
