package org.surface.surface.core.engine.metrics.project.ucam;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class UCAMImpl extends UCAM {

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        return new DoubleMetricValue(getName(), getCode(),
                computeRatio(projectResult.getNumberAllUncalledClassifiedAccessors(), projectResult.getNumberAllClassifiedMethods()));
    }
}
