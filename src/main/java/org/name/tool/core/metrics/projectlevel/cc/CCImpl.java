package org.name.tool.core.metrics.projectlevel.cc;

import org.name.tool.results.MetricValue;
import org.name.tool.results.ProjectAnalyzerResults;

public class CCImpl extends CC {

    @Override
    public MetricValue<Integer> compute(ProjectAnalyzerResults projectResults) {
        int value = projectResults.getCriticalClasses().size();
        return new MetricValue<>(getName(), getCode(), value);
    }
}
