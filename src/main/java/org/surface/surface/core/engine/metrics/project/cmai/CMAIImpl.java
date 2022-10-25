package org.surface.surface.core.engine.metrics.project.cmai;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CMAIImpl extends CMAI {

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        return new DoubleMetricValue(getName(), getCode(),
                computeRatio(projectResult.getNumberAllActualMutatorAttributeInteractions(), projectResult.getNumberAllPossibleMutatorAttributeInteractions()));
    }
}
