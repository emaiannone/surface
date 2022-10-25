package org.surface.surface.core.engine.metrics.project.coa;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class COAImpl extends COA {

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        return new DoubleMetricValue(getName(), getCode(),
                computeRatio(projectResult.getNumberAllNonPrivateClassifiedMethods(), projectResult.getNumberAllClassifiedMethods()));
    }
}
