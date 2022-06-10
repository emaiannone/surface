package org.surface.surface.core.metrics.classlevel.cmw;

import org.surface.surface.core.inspection.results.ClassInspectorResults;
import org.surface.surface.core.metrics.classlevel.cmt.CMT;
import org.surface.surface.core.metrics.results.values.DoubleMetricValue;

public class CMWImpl extends CMW {
    private final CMT CMT;

    public CMWImpl(CMT CMT) {
        this.CMT = CMT;
    }

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        int cmValue = CMT.compute(classResults).getValue();
        int methods = classResults.getAllMethods().size();
        double value = methods != 0.0 ? (double) cmValue / methods : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
