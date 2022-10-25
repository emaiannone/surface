package org.surface.surface.core.engine.metrics.project.cscp;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CSCPImpl extends CSCP {

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        return new DoubleMetricValue(getName(), getCode(),
                computeRatio(projectResult.getNumberSerializableCriticalClasses(), projectResult.getNumberCriticalClasses()));
    }
}
