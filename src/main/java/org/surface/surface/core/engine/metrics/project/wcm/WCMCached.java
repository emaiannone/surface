package org.surface.surface.core.engine.metrics.project.wcm;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.project.caiw.CAIW;
import org.surface.surface.core.engine.metrics.project.cmw.CMW;
import org.surface.surface.core.engine.metrics.project.cwmp.CWMP;
import org.surface.surface.core.engine.metrics.project.ucam.UCAM;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class WCMCached extends WCM {
    private final WCMImpl wcm;
    private DoubleMetricValue cachedResult;

    public WCMCached(CAIW caiw, CMW cmw, CWMP cwmp, UCAM ucam) {
        this.wcm = new WCMImpl(caiw, cmw, cwmp, ucam);
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        if (cachedResult == null) {
            cachedResult = wcm.compute(projectResult);
        }
        return cachedResult;
    }
}
