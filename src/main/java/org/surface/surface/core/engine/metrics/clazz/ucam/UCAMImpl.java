package org.surface.surface.core.engine.metrics.clazz.ucam;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class UCAMImpl extends UCAM {
    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        int numUncalledClassifiedAccessors = classResults.getNumberUncalledClassifiedAccessors();
        int camValue = classResults.getNumberClassifiedMethods();
        double value = camValue != 0.0 ? (double) numUncalledClassifiedAccessors / camValue : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
