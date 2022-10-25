package org.surface.surface.core.engine.metrics.project.cce;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CCEImpl extends CCE {

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        return new DoubleMetricValue(getName(), getCode(),
                computeRatio(projectResult.getNumberNonFinalClassifiedClasses(), projectResult.getNumberCriticalClasses()));
    }
}
