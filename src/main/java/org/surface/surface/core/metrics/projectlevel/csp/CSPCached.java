package org.surface.surface.core.metrics.projectlevel.csp;

import org.surface.surface.core.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.metrics.results.values.DoubleMetricValue;

public class CSPCached extends CSP {
    private final CSPImpl csp;
    private DoubleMetricValue cachedResult;

    public CSPCached() {
        this.csp = new CSPImpl();
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        if (cachedResult == null) {
            cachedResult = csp.compute(projectResults);
        }
        return cachedResult;
    }
}
