package org.surface.surface.core.metrics.projectlevel.sccr;

import org.surface.surface.core.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.metrics.projectlevel.cc.CC;
import org.surface.surface.core.metrics.results.values.DoubleMetricValue;

public class SCCRCached extends SCCR {
    private final SCCRImpl ccr;
    private DoubleMetricValue cachedResult;

    public SCCRCached(CC cc) {
        this.ccr = new SCCRImpl(cc);
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        if (cachedResult == null) {
            cachedResult = ccr.compute(projectResults);
        }
        return cachedResult;
    }
}
