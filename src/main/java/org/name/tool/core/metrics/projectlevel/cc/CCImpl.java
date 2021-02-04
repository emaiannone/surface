package org.name.tool.core.metrics.projectlevel.cc;

import org.name.tool.results.ProjectAnalyzerResults;
import org.name.tool.results.values.IntMetricValue;

public class CCImpl extends CC {

    @Override
    public IntMetricValue compute(ProjectAnalyzerResults projectResults) {
        int value = projectResults.getCriticalClasses().size();
        return new IntMetricValue(getName(), getCode(), value);
    }
}
