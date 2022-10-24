package org.surface.surface.core.engine.metrics.projectlevel.acsp;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class ACSPCached extends ACSP {
    private final ACSPImpl acsp;
    private DoubleMetricValue cachedResult;

    public ACSPCached() {
        this.acsp = new ACSPImpl();
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        if (cachedResult == null) {
            cachedResult = acsp.compute(projectResults);
        }
        return cachedResult;
    }
}
