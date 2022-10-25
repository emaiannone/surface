package org.surface.surface.core.engine.metrics.projectlevel.cdp;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CDPCached extends CDP {
    private final CDPImpl cdp;
    private DoubleMetricValue cachedResult;

    public CDPCached() {
        this.cdp = new CDPImpl();
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        if (cachedResult == null) {
            cachedResult = cdp.compute(projectResults);
        }
        return cachedResult;
    }
}
