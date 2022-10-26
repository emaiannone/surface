package org.surface.surface.core.engine.metrics.project.pfsd;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.project.rca.RCA;
import org.surface.surface.core.engine.metrics.project.rcm.RCM;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class PFSDCached extends PFSD {
    private final PFSDImpl pfsd;
    private DoubleMetricValue cachedResult;

    public PFSDCached(RCA rca, RCM rcm) {
        this.pfsd = new PFSDImpl(rca, rcm);
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        if (cachedResult == null) {
            cachedResult = pfsd.compute(projectResult);
        }
        return cachedResult;
    }
}
