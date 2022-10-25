package org.surface.surface.core.engine.metrics.clazz.coa;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class COAImpl extends COA {

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        int nonPrivate = classResults.getNumberNonPrivateClassifiedMethods();
        int numClassifiedMethods = classResults.getNumberClassifiedMethods();
        double value = numClassifiedMethods != 0 ? (double) nonPrivate / numClassifiedMethods : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
