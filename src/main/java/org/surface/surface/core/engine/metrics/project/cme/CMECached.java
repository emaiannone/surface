package org.surface.surface.core.engine.metrics.project.cme;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CMECached extends CME {
    private final CMEImpl cme;
    private DoubleMetricValue cachedResult;

    public CMECached() {
        this.cme = new CMEImpl();
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        if (cachedResult == null) {
            cachedResult = cme.compute(projectResult);
        }
        return cachedResult;
    }
}
