package org.surface.surface.core.engine.metrics.project.pem;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.project.sam.SAM;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class PEMCached extends PEM {
    private final PEMImpl pem;
    private DoubleMetricValue cachedResult;

    public PEMCached(SAM sam) {
        this.pem = new PEMImpl(sam);
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        if (cachedResult == null) {
            cachedResult = pem.compute(projectResult);
        }
        return cachedResult;
    }
}
