package org.surface.surface.core.engine.metrics.project.cai;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CAICached extends CAI {
    private final CAIImpl cai;
    private DoubleMetricValue cachedResult;

    public CAICached() {
        this.cai = new CAIImpl();
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        if (cachedResult == null) {
            cachedResult = cai.compute(projectResult);
        }
        return cachedResult;
    }
}
