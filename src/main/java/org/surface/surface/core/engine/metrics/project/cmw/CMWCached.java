package org.surface.surface.core.engine.metrics.project.cmw;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CMWCached extends CMW {
    private final CMWImpl cmw;
    private DoubleMetricValue cachedResult;

    public CMWCached() {
        this.cmw = new CMWImpl();
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        if (cachedResult == null) {
            cachedResult = cmw.compute(projectResult);
        }
        return cachedResult;
    }
}
