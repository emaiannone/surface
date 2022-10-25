package org.surface.surface.core.engine.metrics.project.csp;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CSPCached extends CSP {
    private final CSPImpl csp;
    private DoubleMetricValue cachedResult;

    public CSPCached() {
        this.csp = new CSPImpl();
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        if (cachedResult == null) {
            cachedResult = csp.compute(projectResult);
        }
        return cachedResult;
    }
}
