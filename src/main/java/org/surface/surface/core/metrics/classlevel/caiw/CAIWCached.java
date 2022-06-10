package org.surface.surface.core.metrics.classlevel.caiw;

import org.surface.surface.core.inspection.results.ClassInspectorResults;
import org.surface.surface.core.metrics.classlevel.cat.CAT;
import org.surface.surface.core.metrics.results.values.DoubleMetricValue;

public class CAIWCached extends CAIW {
    private final CAIWImpl caiw;
    private DoubleMetricValue cachedResult;

    public CAIWCached(CAT CAT) {
        this.caiw = new CAIWImpl(CAT);
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        if (cachedResult == null) {
            cachedResult = caiw.compute(classResults);
        }
        return cachedResult;
    }
}
