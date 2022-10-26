package org.surface.surface.core.engine.metrics.project.ucam;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class UCAMCached extends UCAM {
    private final UCAMImpl ucam;
    private DoubleMetricValue cachedResult;

    public UCAMCached() {
        this.ucam = new UCAMImpl();
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        if (cachedResult == null) {
            cachedResult = ucam.compute(projectResult);
        }
        return cachedResult;
    }
}
