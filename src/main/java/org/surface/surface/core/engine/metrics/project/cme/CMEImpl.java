package org.surface.surface.core.engine.metrics.project.cme;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;


public class CMEImpl extends CME {

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        int totalCM = projectResults.getNumberAllClassifiedMethods();
        long nonFinalCM = projectResults.getAllClassifiedMethods()
                .stream()
                .filter(md -> !md.isFinal()).count();
        double value = totalCM != 0.0 ? (double) nonFinalCM / totalCM : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
