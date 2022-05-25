package org.surface.surface.core.metrics.projectlevel.sccr;

import org.surface.surface.core.inspection.results.ClassInspectorResults;
import org.surface.surface.core.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.metrics.projectlevel.cc.CC;
import org.surface.surface.core.metrics.results.values.DoubleMetricValue;

public class SCCRImpl extends SCCR {
    private final CC cc;

    public SCCRImpl(CC cc) {
        this.cc = cc;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        int ccValue = cc.compute(projectResults).getValue();
        int serializedClassifiedClasses = 0;
        for (ClassInspectorResults classResults : projectResults.getCriticalClasses()) {
            if (classResults.isSerializable()) {
                serializedClassifiedClasses++;
            }
        }
        double value = ccValue != 0.0 ? (double) serializedClassifiedClasses / ccValue : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
