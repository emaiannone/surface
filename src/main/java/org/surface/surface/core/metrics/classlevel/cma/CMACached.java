package org.surface.surface.core.metrics.classlevel.cma;

import org.surface.surface.core.inspection.results.ClassInspectorResults;
import org.surface.surface.core.metrics.classlevel.cm.CM;
import org.surface.surface.core.metrics.results.values.DoubleMetricValue;

public class CMACached extends CMA {
    private final CMAImpl cma;
    private DoubleMetricValue cachedResult;

    public CMACached(CM cm) {
        this.cma = new CMAImpl(cm);
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        if (cachedResult == null) {
            cachedResult = cma.compute(classResults);
        }
        return cachedResult;
    }
}
