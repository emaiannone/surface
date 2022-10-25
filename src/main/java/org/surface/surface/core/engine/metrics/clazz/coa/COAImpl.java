package org.surface.surface.core.engine.metrics.clazz.coa;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class COAImpl extends COA {

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResult) {
        return new DoubleMetricValue(getName(), getCode(),
                computeRatio(classResult.getNumberNonPrivateClassifiedMethods(), classResult.getNumberClassifiedMethods()));
    }
}
