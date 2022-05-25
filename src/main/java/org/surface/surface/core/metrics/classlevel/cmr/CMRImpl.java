package org.surface.surface.core.metrics.classlevel.cmr;

import org.surface.surface.core.inspection.results.ClassInspectorResults;
import org.surface.surface.core.metrics.classlevel.cm.CM;
import org.surface.surface.core.metrics.results.values.DoubleMetricValue;

public class CMRImpl extends CMR {
    private final CM cm;

    public CMRImpl(CM cm) {
        this.cm = cm;
    }

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        int cmValue = cm.compute(classResults).getValue();
        int methods = classResults.getAllMethods().size();
        double value = methods != 0.0 ? (double) cmValue / methods : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
