package org.surface.surface.core.engine.metrics.project.ccc;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CCCImpl extends CCC {

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        return new DoubleMetricValue(getName(), getCode(),
                computeRatio(projectResult.getNumberClassesAccessingClassifiedAttributes(), projectResult.getNumberPossibleAccesses()));
    }
}
