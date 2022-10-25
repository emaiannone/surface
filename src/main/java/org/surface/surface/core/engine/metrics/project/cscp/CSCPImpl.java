package org.surface.surface.core.engine.metrics.project.cscp;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CSCPImpl extends CSCP {

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        int cctValue = projectResult.getNumberCriticalClasses();
        int serializedClassifiedClasses = projectResult.getNumberSerializableCriticalClasses();
        double value = cctValue != 0.0 ? (double) serializedClassifiedClasses / cctValue : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
