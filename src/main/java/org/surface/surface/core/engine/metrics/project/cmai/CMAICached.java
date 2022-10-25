package org.surface.surface.core.engine.metrics.project.cmai;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CMAICached extends CMAI {
    private final CMAIImpl cmai;
    private DoubleMetricValue cachedResult;

    public CMAICached() {
        this.cmai = new CMAIImpl();
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        if (cachedResult == null) {
            cachedResult = cmai.compute(projectResult);
        }
        return cachedResult;
    }
}
