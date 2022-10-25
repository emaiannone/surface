package org.surface.surface.core.engine.metrics.classlevel.cwmp;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CWMPImpl extends CWMP {
    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        int numMutators = classResults.getNumberClassifiedMutators();
        int methods = classResults.getNumberMethods();
        double value = methods != 0.0 ? (double) numMutators / methods : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
