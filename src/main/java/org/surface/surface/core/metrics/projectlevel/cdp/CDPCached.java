package org.surface.surface.core.metrics.projectlevel.cdp;

import org.surface.surface.core.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.metrics.projectlevel.cct.CCT;
import org.surface.surface.core.metrics.results.values.DoubleMetricValue;

public class CDPCached extends CDP {
    private final CDPImpl cdp;
    private DoubleMetricValue cachedResult;

    public CDPCached(CCT CCT) {
        this.cdp = new CDPImpl(CCT);
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        if (cachedResult == null) {
            cachedResult = cdp.compute(projectResults);
        }
        return cachedResult;
    }
}
