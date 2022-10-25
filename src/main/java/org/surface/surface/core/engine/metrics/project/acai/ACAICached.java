package org.surface.surface.core.engine.metrics.project.acai;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class ACAICached extends ACAI {
    private final ACAIImpl acai;
    private DoubleMetricValue cachedResult;

    public ACAICached() {
        this.acai = new ACAIImpl();
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        if (cachedResult == null) {
            cachedResult = acai.compute(projectResults);
        }
        return cachedResult;
    }
}
