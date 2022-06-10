package org.surface.surface.core.metrics.projectlevel.cce;

import org.surface.surface.core.inspection.results.ClassInspectorResults;
import org.surface.surface.core.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.metrics.projectlevel.cct.CCT;
import org.surface.surface.core.metrics.results.values.DoubleMetricValue;

public class CCEImpl extends CCE {
    private final CCT CCT;

    public CCEImpl(CCT CCT) {
        this.CCT = CCT;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        int nonFinalCC = 0;
        for (ClassInspectorResults classResults : projectResults) {
            if (classResults.isCritical() && !classResults.isFinal()) {
                nonFinalCC++;
            }
        }
        int ccValue = CCT.compute(projectResults).getValue();
        double value = ccValue != 0.0 ? (double) nonFinalCC / ccValue : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
