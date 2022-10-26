package org.surface.surface.core.engine.metrics.project.pi;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.project.wcc.WCC;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class PICached extends PI {
    private final PIImpl pi;
    private DoubleMetricValue cachedResult;

    public PICached(WCC wcc) {
        this.pi = new PIImpl(wcc);
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        if (cachedResult == null) {
            cachedResult = pi.compute(projectResult);
        }
        return cachedResult;
    }
}
