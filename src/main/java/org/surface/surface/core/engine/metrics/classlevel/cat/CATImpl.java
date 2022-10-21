package org.surface.surface.core.engine.metrics.classlevel.cat;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.IntMetricValue;

public class CATImpl extends CAT {

    @Override
    public IntMetricValue compute(ClassInspectorResults classResults) {
        int value = classResults.getNumberClassifiedAttributes();
        return new IntMetricValue(getName(), getCode(), value);
    }
}
