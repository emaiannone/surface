package org.surface.surface.core.metrics.classlevel.cmt;

import org.surface.surface.core.inspection.results.ClassInspectorResults;
import org.surface.surface.core.metrics.results.values.IntMetricValue;

public class CMTImpl extends CMT {

    @Override
    public IntMetricValue compute(ClassInspectorResults classResults) {
        int value = classResults.getAllClassifiedMethods().size();
        return new IntMetricValue(getName(), getCode(), value);
    }
}
