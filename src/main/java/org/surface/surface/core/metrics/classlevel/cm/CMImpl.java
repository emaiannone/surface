package org.surface.surface.core.metrics.classlevel.cm;

import org.surface.surface.core.inspection.results.ClassInspectorResults;
import org.surface.surface.core.metrics.results.values.IntMetricValue;

public class CMImpl extends CM {

    @Override
    public IntMetricValue compute(ClassInspectorResults classResults) {
        int value = classResults.getAllClassifiedMethods().size();
        return new IntMetricValue(getName(), getCode(), value);
    }
}
