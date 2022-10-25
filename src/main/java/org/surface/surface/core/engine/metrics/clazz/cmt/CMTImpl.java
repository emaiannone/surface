package org.surface.surface.core.engine.metrics.clazz.cmt;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.IntMetricValue;

public class CMTImpl extends CMT {

    @Override
    public IntMetricValue compute(ClassInspectorResults classResult) {
        return new IntMetricValue(getName(), getCode(),
                classResult.getNumberClassifiedMethods());
    }
}
