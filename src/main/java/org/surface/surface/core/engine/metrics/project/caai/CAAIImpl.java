package org.surface.surface.core.engine.metrics.project.caai;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CAAIImpl extends CAAI {

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        return new DoubleMetricValue(getName(), getCode(),
                computeRatio(projectResult.getNumberAllActualAccessorAttributeInteractions(), projectResult.getNumberAllPossibleAccessorAttributeInteractions()));
    }
}
