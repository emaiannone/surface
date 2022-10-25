package org.surface.surface.core.engine.metrics.project.cpcc;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CPCCImpl extends CPCC {

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        int numNestedCriticalClasses = projectResult.getNumberNestedCriticalClasses();
        int numCriticalClassesWithNested = projectResult.getNumberCriticalClassesWithNested();
        int denom = numNestedCriticalClasses + numCriticalClassesWithNested;
        double value = denom != 0.0 ? 1.0 - ((double) numNestedCriticalClasses / denom) : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
