package org.surface.surface.core.engine.metrics.project.cdp;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CDPImpl extends CDP {
    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        int numClasses = projectResults.getNumberClasses();
        int cctValue = projectResults.getNumberCriticalClasses();
        double value = numClasses != 0.0 ? (double) cctValue / numClasses : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
