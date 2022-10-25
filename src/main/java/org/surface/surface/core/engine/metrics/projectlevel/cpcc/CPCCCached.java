package org.surface.surface.core.engine.metrics.projectlevel.cpcc;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CPCCCached extends CPCC {
    private final CPCCImpl cpcc;
    private DoubleMetricValue cachedResult;

    public CPCCCached() {
        this.cpcc = new CPCCImpl();
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        if (cachedResult == null) {
            cachedResult = cpcc.compute(projectResults);
        }
        return cachedResult;
    }
}
