package org.surface.surface.core.engine.metrics.project.caiw;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CAIWImpl extends CAIW {

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        return new DoubleMetricValue(getName(), getCode(),
                computeRatio(projectResult.getNumberAllActualAttributeInteractions(), projectResult.getNumberAllPossibleAttributeInteractions()));
    }
}
