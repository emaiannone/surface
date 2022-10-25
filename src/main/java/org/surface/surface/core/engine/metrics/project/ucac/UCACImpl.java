package org.surface.surface.core.engine.metrics.project.ucac;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class UCACImpl extends UCAC {

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        int numCriticalClasses = projectResult.getNumberCriticalClasses();
        int numCriticalClassesUncalledAccessor = projectResult.getNumberCriticalClassesUncalledAccessor();
        double value = numCriticalClasses != 0.0 ? (double) numCriticalClassesUncalledAccessor / numCriticalClasses : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
