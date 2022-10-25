package org.surface.surface.core.engine.metrics.clazz.cmw;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CMWCached extends CMW {
    private final CMWImpl cmw;
    private DoubleMetricValue cachedResult;

    public CMWCached() {
        this.cmw = new CMWImpl();
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        if (cachedResult == null) {
            cachedResult = cmw.compute(classResults);
        }
        return cachedResult;
    }
}
