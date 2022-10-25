package org.surface.surface.core.engine.metrics.project.csp;

import org.surface.surface.core.engine.inspection.results.InheritanceInspectorResults;
import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CSPImpl extends CSP {

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResult) {
        InheritanceInspectorResults inheritanceResult = projectResult.getInheritanceResult();
        return new DoubleMetricValue(getName(), getCode(),
                computeRatio(inheritanceResult.getNumberAllCriticalSuperclasses(), inheritanceResult.getNumberAllCriticalClasses()));
    }
}
