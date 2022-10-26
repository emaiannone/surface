package org.surface.surface.core.engine.metrics.project.cwmp;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CWMPImpl extends CWMP {

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        return new DoubleMetricValue(getName(), getCode(),
                computeRatio(projectResult.getNumberAllClassifiedMutators(), projectResult.getNumberAllMethods()));
    }
}
