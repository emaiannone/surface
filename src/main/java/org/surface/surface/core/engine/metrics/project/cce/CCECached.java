package org.surface.surface.core.engine.metrics.project.cce;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CCECached extends CCE {
    private final CCEImpl cce;
    private DoubleMetricValue cachedResult;

    public CCECached() {
        this.cce = new CCEImpl();
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        if (cachedResult == null) {
            cachedResult = cce.compute(projectResults);
        }
        return cachedResult;
    }
}
