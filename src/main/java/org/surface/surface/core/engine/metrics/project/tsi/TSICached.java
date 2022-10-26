package org.surface.surface.core.engine.metrics.project.tsi;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.project.pem.PEM;
import org.surface.surface.core.engine.metrics.project.pfsd.PFSD;
import org.surface.surface.core.engine.metrics.project.pi.PI;
import org.surface.surface.core.engine.metrics.project.plcm.PLCM;
import org.surface.surface.core.engine.metrics.project.plp.PLP;
import org.surface.surface.core.engine.metrics.project.pras.PRAS;
import org.surface.surface.core.engine.metrics.project.pswl.PSWL;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class TSICached extends TSI {
    private final TSIImpl tsi;
    private DoubleMetricValue cachedResult;

    public TSICached(PLP plp, PRAS pras, PSWL pswl, PFSD pfsd, PLCM plcm, PI pi, PEM pem) {
        this.tsi = new TSIImpl(plp, pras, pswl, pfsd, plcm, pi, pem);
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        if (cachedResult == null) {
            cachedResult = tsi.compute(projectResult);
        }
        return cachedResult;
    }
}
