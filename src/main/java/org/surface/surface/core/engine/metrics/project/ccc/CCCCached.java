package org.surface.surface.core.engine.metrics.project.ccc;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CCCCached extends CCC {
    private final CCCImpl ccc;
    private DoubleMetricValue cachedResult;

    public CCCCached() {
        this.ccc = new CCCImpl();
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        if (cachedResult == null) {
            cachedResult = ccc.compute(projectResult);
        }
        return cachedResult;
    }
}
