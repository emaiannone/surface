package org.surface.surface.core.engine.metrics.classlevel.cmw;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CMWImpl extends CMW {

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        int cmValue = classResults.getNumberClassifiedMethods();
        int methods = classResults.getNumberMethods();
        double value = methods != 0.0 ? (double) cmValue / methods : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
