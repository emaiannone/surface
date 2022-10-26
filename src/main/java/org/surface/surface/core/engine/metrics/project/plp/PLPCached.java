package org.surface.surface.core.engine.metrics.project.plp;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.project.wca.WCA;
import org.surface.surface.core.engine.metrics.project.wcc.WCC;
import org.surface.surface.core.engine.metrics.project.wcm.WCM;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class PLPCached extends PLP {
    private final PLPImpl plp;
    private DoubleMetricValue cachedResult;

    public PLPCached(WCA wca, WCM wcm, WCC wcc) {
        this.plp = new PLPImpl(wca, wcm, wcc);
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        if (cachedResult == null) {
            cachedResult = plp.compute(projectResult);
        }
        return cachedResult;
    }
}
