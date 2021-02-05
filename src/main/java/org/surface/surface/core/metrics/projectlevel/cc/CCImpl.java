package org.surface.surface.core.metrics.projectlevel.cc;

import org.surface.surface.results.ProjectAnalyzerResults;
import org.surface.surface.results.values.IntMetricValue;

public class CCImpl extends CC {

    @Override
    public IntMetricValue compute(ProjectAnalyzerResults projectResults) {
        int value = projectResults.getCriticalClasses().size();
        return new IntMetricValue(getName(), getCode(), value);
    }
}
