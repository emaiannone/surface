package org.surface.surface.core.engine.metrics.project.ucac;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class UCACImpl extends UCAC {

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        return new DoubleMetricValue(getName(), getCode(),
                computeRatio(projectResult.getNumberCriticalClassesUncalledAccessor(), projectResult.getNumberCriticalClasses()));
    }
}
