package org.surface.surface.core.engine.metrics.project.csi;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CSICached extends CSI {
    private final CSIImpl csi;
    private DoubleMetricValue cachedResult;

    public CSICached() {
        this.csi = new CSIImpl();
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        if (cachedResult == null) {
            cachedResult = csi.compute(projectResults);
        }
        return cachedResult;
    }
}
