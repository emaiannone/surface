package org.surface.surface.core.engine.metrics.project.cat;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.IntMetricValue;

public class CATCached extends CAT {
    private final CATImpl cat;
    private IntMetricValue cachedResult;

    public CATCached() {
        this.cat = new CATImpl();
        this.cachedResult = null;
    }

    @Override
    public IntMetricValue compute(ProjectInspectorResults projectResult) {
        if (cachedResult == null) {
            cachedResult = cat.compute(projectResult);
        }
        return cachedResult;
    }
}
