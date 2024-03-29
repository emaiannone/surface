package org.surface.surface.core.engine.metrics.project.cscp;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CSCPCached extends CSCP {
    private final CSCPImpl cscp;
    private DoubleMetricValue cachedResult;

    public CSCPCached() {
        this.cscp = new CSCPImpl();
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        if (cachedResult == null) {
            cachedResult = cscp.compute(projectResult);
        }
        return cachedResult;
    }
}
