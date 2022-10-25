package org.surface.surface.core.engine.metrics.project.cai;

import org.surface.surface.core.engine.inspection.results.InheritanceInspectorResults;
import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CAIImpl extends CAI {

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        InheritanceInspectorResults inheritanceResult = projectResult.getInheritanceResult();
        return new DoubleMetricValue(getName(), getCode(),
                computeRatio(inheritanceResult.getNumberAllInheritableClassifiedAttributes(), inheritanceResult.getNumberAllClassifiedAttributes()));
    }
}
