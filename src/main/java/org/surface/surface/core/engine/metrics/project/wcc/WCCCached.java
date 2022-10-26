package org.surface.surface.core.engine.metrics.project.wcc;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.project.ccc.CCC;
import org.surface.surface.core.engine.metrics.project.cscp.CSCP;
import org.surface.surface.core.engine.metrics.project.csi.CSI;
import org.surface.surface.core.engine.metrics.project.ucac.UCAC;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class WCCCached extends WCC {
    private final WCCImpl wcc;
    private DoubleMetricValue cachedResult;

    public WCCCached(CCC ccc, UCAC ucac, CSCP cscp, CSI csi) {
        this.wcc = new WCCImpl(ccc, ucac, cscp, csi);
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        if (cachedResult == null) {
            cachedResult = wcc.compute(projectResult);
        }
        return cachedResult;
    }
}
