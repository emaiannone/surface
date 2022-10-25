package org.surface.surface.core.engine.metrics.project.cida;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CIDAImpl extends CIDA {

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        return new DoubleMetricValue(getName(), getCode(),
                computeRatio(projectResult.getNumberAllNonPrivateInstanceClassifiedAttributes(), projectResult.getNumberAllClassifiedAttributes()));
    }
}
