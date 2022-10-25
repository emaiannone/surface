package org.surface.surface.core.engine.metrics.projectlevel.cpcc;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CPCCImpl extends CPCC {

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        int numNestedCriticalClasses = projectResults.getNumberNestedCriticalClasses();
        int numCriticalClassesWithNested = projectResults.getNumberCriticalClassesWithNested();
        int denom = numNestedCriticalClasses + numCriticalClassesWithNested;
        double value = denom != 0.0 ? 1.0 - ((double) numNestedCriticalClasses / denom) : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
