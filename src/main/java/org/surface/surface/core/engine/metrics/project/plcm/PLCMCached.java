package org.surface.surface.core.engine.metrics.project.plcm;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.project.rcc.RCC;
import org.surface.surface.core.engine.metrics.project.wcc.WCC;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class PLCMCached extends PLCM {
    private final PLCMImpl plcm;
    private DoubleMetricValue cachedResult;

    public PLCMCached(RCC rcc, WCC wcc) {
        this.plcm = new PLCMImpl(rcc, wcc);
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        if (cachedResult == null) {
            cachedResult = plcm.compute(projectResult);
        }
        return cachedResult;
    }
}
