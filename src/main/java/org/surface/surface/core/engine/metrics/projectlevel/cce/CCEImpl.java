package org.surface.surface.core.engine.metrics.projectlevel.cce;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CCEImpl extends CCE {

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        long nonFinalCc = projectResults.getClassResults()
                .stream()
                .filter(cr -> cr.isCritical() && !cr.isFinal())
                .count();
        int cctValue = projectResults.getNumberCriticalClasses();
        double value = cctValue != 0.0 ? (double) nonFinalCc / cctValue : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
