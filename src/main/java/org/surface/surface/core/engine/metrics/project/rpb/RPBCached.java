package org.surface.surface.core.engine.metrics.project.rpb;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.BooleanMetricValue;

public class RPBCached extends RPB {
    private final RPBImpl rpb;
    private BooleanMetricValue cachedResult;

    public RPBCached() {
        this.rpb = new RPBImpl();
        this.cachedResult = null;
    }

    @Override
    public BooleanMetricValue compute(ProjectInspectorResults projectResult) {
        if (cachedResult == null) {
            cachedResult = rpb.compute(projectResult);
        }
        return cachedResult;
    }
}
