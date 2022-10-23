package org.surface.surface.core.engine.metrics.classlevel.cwmp;

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
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        if (cachedResult == null) {
            cachedResult = cwmp.compute(classResults);
        }
        return cachedResult;
    }
}
