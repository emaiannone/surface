package org.name.tool.core.metrics.projectlevel.cc;

import org.name.tool.results.MetricResult;
import org.name.tool.results.ProjectAnalyzerResults;

public class CCImpl extends CC {

    @Override
    public MetricResult<Integer> compute(ProjectAnalyzerResults projectResults) {
        int value = projectResults.getCriticalClasses().size();
        return new MetricResult<>(getName(), getCode(), value);
    }
}
