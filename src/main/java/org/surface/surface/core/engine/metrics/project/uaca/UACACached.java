package org.surface.surface.core.engine.metrics.project.uaca;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class UACACached extends UACA {
    private final UACAImpl uaca;
    private DoubleMetricValue cachedResult;

    public UACACached() {
        this.uaca = new UACAImpl();
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        if (cachedResult == null) {
            cachedResult = uaca.compute(projectResult);
        }
        return cachedResult;
    }
}
