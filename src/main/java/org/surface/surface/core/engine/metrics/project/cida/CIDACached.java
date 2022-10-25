package org.surface.surface.core.engine.metrics.project.cida;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CIDACached extends CIDA {
    private final CIDAImpl cida;
    private DoubleMetricValue cachedResult;

    public CIDACached() {
        this.cida = new CIDAImpl();
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        if (cachedResult == null) {
            cachedResult = cida.compute(projectResult);
        }
        return cachedResult;
    }

}
