package org.surface.surface.core.engine.metrics.project.coa;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class COACached extends COA {
    private final COAImpl coa;
    private DoubleMetricValue cachedResult;

    public COACached() {
        this.coa = new COAImpl();
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        if (cachedResult == null) {
            cachedResult = coa.compute(projectResult);
        }
        return cachedResult;
    }
}
