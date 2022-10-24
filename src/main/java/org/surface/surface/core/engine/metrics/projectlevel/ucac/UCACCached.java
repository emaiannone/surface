package org.surface.surface.core.engine.metrics.projectlevel.ucac;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class UCACCached extends UCAC {
    private final UCACImpl ucac;
    private DoubleMetricValue cachedResult;

    public UCACCached() {
        this.ucac = new UCACImpl();
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        if (cachedResult == null) {
            cachedResult = ucac.compute(projectResults);
        }
        return cachedResult;
    }
}
