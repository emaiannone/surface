package org.surface.surface.core.engine.metrics.project.pswl;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.project.wca.WCA;
import org.surface.surface.core.engine.metrics.project.wcm.WCM;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class PSWLCached extends PSWL {
    private final PSWLImpl pswl;
    private DoubleMetricValue cachedResult;

    public PSWLCached(WCA wca, WCM wcm) {
        this.pswl = new PSWLImpl(wca, wcm);
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        if (cachedResult == null) {
            cachedResult = pswl.compute(projectResult);
        }
        return cachedResult;
    }
}
