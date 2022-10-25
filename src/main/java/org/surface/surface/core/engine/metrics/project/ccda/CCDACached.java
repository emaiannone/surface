package org.surface.surface.core.engine.metrics.project.ccda;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CCDACached extends CCDA {
    private final CCDAImpl ccda;
    private DoubleMetricValue cachedResult;

    public CCDACached() {
        this.ccda = new CCDAImpl();
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        if (cachedResult == null) {
            cachedResult = ccda.compute(projectResult);
        }
        return cachedResult;
    }
}
