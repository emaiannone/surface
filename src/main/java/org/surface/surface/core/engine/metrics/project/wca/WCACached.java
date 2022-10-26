package org.surface.surface.core.engine.metrics.project.wca;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.project.caai.CAAI;
import org.surface.surface.core.engine.metrics.project.cmai.CMAI;
import org.surface.surface.core.engine.metrics.project.uaca.UACA;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class WCACached extends WCA {
    private final WCAImpl wca;
    private DoubleMetricValue cachedResult;

    public WCACached(CMAI cmai, CAAI caai, UACA uaca) {
        this.wca = new WCAImpl(cmai, caai, uaca);
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        if (cachedResult == null) {
            cachedResult = wca.compute(projectResult);
        }
        return cachedResult;
    }
}
