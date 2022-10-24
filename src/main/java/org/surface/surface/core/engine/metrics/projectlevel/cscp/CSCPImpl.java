package org.surface.surface.core.engine.metrics.projectlevel.cscp;

import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.projectlevel.cct.CCT;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CSCPImpl extends CSCP {
    private final CCT CCT;

    public CSCPImpl(CCT CCT) {
        this.CCT = CCT;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        int ccValue = CCT.compute(projectResults).getValue();
        int serializedClassifiedClasses = projectResults.getNumberSerializableCriticalClasses();
        double value = ccValue != 0.0 ? (double) serializedClassifiedClasses / ccValue : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
