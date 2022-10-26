package org.surface.surface.core.engine.metrics.project.rcc;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.project.cce.CCE;
import org.surface.surface.core.engine.metrics.project.cdp.CDP;
import org.surface.surface.core.engine.metrics.project.cpcc.CPCC;
import org.surface.surface.core.engine.metrics.project.csp.CSP;
import org.surface.surface.core.engine.metrics.project.rpb.RPB;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class RCCCached extends RCC {
    private final RCCImpl rcc;
    private DoubleMetricValue cachedResult;

    public RCCCached(RPB rpb, CPCC cpcc, CCE cce, CDP cdp, CSP csp) {
        this.rcc = new RCCImpl(rpb, cpcc, cce, cdp, csp);
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        if (cachedResult == null) {
            cachedResult = rcc.compute(projectResult);
        }
        return cachedResult;
    }
}
