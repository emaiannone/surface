package org.surface.surface.core.engine.metrics.projectlevel.acmi;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class ACMICached extends ACMI {
    private final ACMIImpl acmi;
    private DoubleMetricValue cachedResult;

    public ACMICached() {
        this.acmi = new ACMIImpl();
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        if (cachedResult == null) {
            cachedResult = acmi.compute(projectResults);
        }
        return cachedResult;
    }
}
