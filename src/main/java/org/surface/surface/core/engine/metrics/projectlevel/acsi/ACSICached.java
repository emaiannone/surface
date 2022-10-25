package org.surface.surface.core.engine.metrics.projectlevel.acsi;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class ACSICached extends ACSI {
    private final ACSIImpl acsi;
    private DoubleMetricValue cachedResult;

    public ACSICached() {
        this.acsi = new ACSIImpl();
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        if (cachedResult == null) {
            cachedResult = acsi.compute(projectResults);
        }
        return cachedResult;
    }
}
