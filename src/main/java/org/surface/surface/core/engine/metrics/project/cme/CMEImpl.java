package org.surface.surface.core.engine.metrics.project.cme;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;


public class CMEImpl extends CME {

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        return new DoubleMetricValue(getName(), getCode(),
                computeRatio(projectResult.getNumberAllNonFinalClassifiedMethods(), projectResult.getNumberAllClassifiedMethods()));
    }
}
