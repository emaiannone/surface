package org.surface.surface.core.engine.metrics.project.ccda;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CCDAImpl extends CCDA {

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        return new DoubleMetricValue(getName(), getCode(),
                computeRatio(projectResult.getNumberAllNonPrivateClassClassifiedAttributes(), projectResult.getNumberAllClassifiedAttributes()));
    }
}
