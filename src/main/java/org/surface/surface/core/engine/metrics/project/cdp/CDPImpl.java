package org.surface.surface.core.engine.metrics.project.cdp;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CDPImpl extends CDP {
    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        int numClasses = projectResult.getNumberClasses();
        int cctValue = projectResult.getNumberCriticalClasses();
        double value = numClasses != 0.0 ? (double) cctValue / numClasses : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
