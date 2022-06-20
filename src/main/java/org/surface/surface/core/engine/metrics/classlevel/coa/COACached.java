package org.surface.surface.core.engine.metrics.classlevel.coa;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.classlevel.cmt.CMT;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class COACached extends COA {
    private final COAImpl coa;
    private DoubleMetricValue cachedResult;

    public COACached(CMT CMT) {
        this.coa = new COAImpl(CMT);
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        if (cachedResult == null) {
            cachedResult = coa.compute(classResults);
        }
        return cachedResult;
    }
}
