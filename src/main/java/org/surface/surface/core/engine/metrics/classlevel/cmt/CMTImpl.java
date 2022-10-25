package org.surface.surface.core.engine.metrics.classlevel.cmt;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.IntMetricValue;

public class CMTImpl extends CMT {

    @Override
    public IntMetricValue compute(ClassInspectorResults classResults) {
        int value = classResults.getNumberClassifiedMethods();
        return new IntMetricValue(getName(), getCode(), value);
    }
}
