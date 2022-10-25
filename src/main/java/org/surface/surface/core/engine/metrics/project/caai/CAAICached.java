package org.surface.surface.core.engine.metrics.project.caai;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CAAICached extends CAAI {
    private final CAAIImpl caai;
    private DoubleMetricValue cachedResult;

    public CAAICached() {
        this.caai = new CAAIImpl();
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        if (cachedResult == null) {
            cachedResult = caai.compute(projectResult);
        }
        return cachedResult;
    }
}
