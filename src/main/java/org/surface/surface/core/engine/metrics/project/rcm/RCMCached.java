package org.surface.surface.core.engine.metrics.project.rcm;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.project.cme.CME;
import org.surface.surface.core.engine.metrics.project.cmi.CMI;
import org.surface.surface.core.engine.metrics.project.coa.COA;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class RCMCached extends RCM {
    private final RCMImpl rcm;
    private DoubleMetricValue cachedResult;

    public RCMCached(COA coa, CME cme, CMI cmi) {
        this.rcm = new RCMImpl(coa, cme, cmi);
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        if (cachedResult == null) {
            cachedResult = rcm.compute(projectResult);
        }
        return cachedResult;
    }
}
