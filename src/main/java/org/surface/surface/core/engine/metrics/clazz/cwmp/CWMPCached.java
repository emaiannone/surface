package org.surface.surface.core.engine.metrics.clazz.cwmp;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CWMPCached extends CWMP {
    private final CWMPImpl cwmp;
    private DoubleMetricValue cachedResult;

    public CWMPCached() {
        this.cwmp = new CWMPImpl();
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResult) {
        if (cachedResult == null) {
            cachedResult = cwmp.compute(classResult);
        }
        return cachedResult;
    }
}
