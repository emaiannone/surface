package org.surface.surface.core.engine.metrics.clazz.ucam;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class UCAMImpl extends UCAM {

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResult) {
        return new DoubleMetricValue(getName(), getCode(),
                computeRatio(classResult.getNumberUncalledClassifiedAccessors(), classResult.getNumberClassifiedMethods()));
    }
}
