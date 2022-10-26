package org.surface.surface.core.engine.metrics.project.pras;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.project.rca.RCA;
import org.surface.surface.core.engine.metrics.project.rcc.RCC;
import org.surface.surface.core.engine.metrics.project.rcm.RCM;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class PRASCached extends PRAS {
    private final PRASImpl pras;
    private DoubleMetricValue cachedResult;

    public PRASCached(RCA rca, RCM rcm, RCC rcc) {
        this.pras = new PRASImpl(rca, rcm, rcc);
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        if (cachedResult == null) {
            cachedResult = pras.compute(projectResult);
        }
        return cachedResult;
    }
}
