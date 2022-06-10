package org.surface.surface.core.metrics.projectlevel.cscp;

import org.surface.surface.core.inspection.results.ClassInspectorResults;
import org.surface.surface.core.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.metrics.projectlevel.cct.CCT;
import org.surface.surface.core.metrics.results.values.DoubleMetricValue;

public class CSCPImpl extends CSCP {
    private final CCT CCT;

    public CSCPImpl(CCT CCT) {
        this.CCT = CCT;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        int ccValue = CCT.compute(projectResults).getValue();
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
