package org.surface.surface.core.engine.metrics.project.rca;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.project.cai.CAI;
import org.surface.surface.core.engine.metrics.project.ccda.CCDA;
import org.surface.surface.core.engine.metrics.project.cida.CIDA;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class RCACached extends RCA {
    private final RCAImpl rca;
    private DoubleMetricValue cachedResult;

    public RCACached(CIDA cida, CCDA ccda, CAI cai) {
        this.rca = new RCAImpl(cida, ccda, cai);
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        if (cachedResult == null) {
            cachedResult = rca.compute(projectResult);
        }
        return cachedResult;
    }
}
