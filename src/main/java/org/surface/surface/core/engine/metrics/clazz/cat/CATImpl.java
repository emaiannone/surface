package org.surface.surface.core.engine.metrics.clazz.cat;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.IntMetricValue;

public class CATImpl extends CAT {

    @Override
    public IntMetricValue compute(ClassInspectorResults classResult) {
        return new IntMetricValue(getName(), getCode(),
                classResult.getNumberClassifiedAttributes());
    }
}
