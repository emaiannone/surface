package org.surface.surface.core.metrics.classlevel.cm;

import org.surface.surface.core.inspection.results.ClassInspectorResults;
import org.surface.surface.core.metrics.results.values.IntMetricValue;

public class CMCached extends CM {
    private final CMImpl classifiedMethods;
    private IntMetricValue cachedResult;

    public CMCached() {
        this.classifiedMethods = new CMImpl();
        this.cachedResult = null;
    }

    @Override
    public IntMetricValue compute(ClassInspectorResults classResults) {
        if (cachedResult == null) {
            cachedResult = classifiedMethods.compute(classResults);
        }
        return cachedResult;
    }
}
