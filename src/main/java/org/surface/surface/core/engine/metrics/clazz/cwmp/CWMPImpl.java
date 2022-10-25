package org.surface.surface.core.engine.metrics.clazz.cwmp;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CWMPImpl extends CWMP {

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResult) {
        return new DoubleMetricValue(getName(), getCode(),
                computeRatio(classResult.getNumberClassifiedMutators(), classResult.getNumberMethods()));
    }
}
