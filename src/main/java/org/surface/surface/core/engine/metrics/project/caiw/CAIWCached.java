package org.surface.surface.core.engine.metrics.project.caiw;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CAIWCached extends CAIW {
    private final CAIWImpl caiw;
    private DoubleMetricValue cachedResult;

    public CAIWCached() {
        this.caiw = new CAIWImpl();
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        if (cachedResult == null) {
            cachedResult = caiw.compute(projectResult);
        }
        return cachedResult;
    }
}
