package org.surface.surface.core.engine.metrics.clazz.cmw;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CMWImpl extends CMW {

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        int numClassifiedMethods = classResults.getNumberClassifiedMethods();
        int numMethods = classResults.getNumberMethods();
        double value = numMethods != 0 ? (double) numClassifiedMethods / numMethods : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
