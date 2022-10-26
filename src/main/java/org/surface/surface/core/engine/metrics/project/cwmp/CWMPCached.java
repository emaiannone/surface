package org.surface.surface.core.engine.metrics.project.cwmp;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CWMPCached extends CWMP {
    private final CWMPImpl cwmp;
    private DoubleMetricValue cachedResult;

    public CWMPCached() {
        this.cwmp = new CWMPImpl();
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        if (cachedResult == null) {
            cachedResult = cwmp.compute(projectResult);
        }
        return cachedResult;
    }
}
