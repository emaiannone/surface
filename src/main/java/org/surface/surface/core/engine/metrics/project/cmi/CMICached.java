package org.surface.surface.core.engine.metrics.project.cmi;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CMICached extends CMI {
    private final CMIImpl cmi;
    private DoubleMetricValue cachedResult;

    public CMICached() {
        this.cmi = new CMIImpl();
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        if (cachedResult == null) {
            cachedResult = cmi.compute(projectResults);
        }
        return cachedResult;
    }
}
