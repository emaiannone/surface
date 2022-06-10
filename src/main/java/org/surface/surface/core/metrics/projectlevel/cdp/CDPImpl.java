package org.surface.surface.core.metrics.projectlevel.cdp;

import org.surface.surface.core.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.metrics.projectlevel.cct.CCT;
import org.surface.surface.core.metrics.results.values.DoubleMetricValue;

public class CDPImpl extends CDP {
    private final CCT CCT;

    public CDPImpl(CCT CCT) {
        this.CCT = CCT;
    }

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        int numClasses = projectResults.getResults().size();
        int ccValue = CCT.compute(projectResults).getValue();
        double value = numClasses != 0.0 ? (double) ccValue / numClasses : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
