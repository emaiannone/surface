package org.surface.surface.core.metrics.projectlevel.cct;

import org.surface.surface.core.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.metrics.results.values.IntMetricValue;

public class CCTImpl extends CCT {

    @Override
    public IntMetricValue compute(ProjectInspectorResults projectResults) {
        int value = projectResults.getCriticalClasses().size();
        return new IntMetricValue(getName(), getCode(), value);
    }
}
