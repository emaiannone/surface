package org.surface.surface.core.engine.metrics.project.uaca;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class UACAImpl extends UACA {

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        return new DoubleMetricValue(getName(), getCode(),
                computeRatio(projectResult.getNumberAllUnaccessedAssignedAttributes(), projectResult.getNumberAllClassifiedAttributes()));
    }
}
