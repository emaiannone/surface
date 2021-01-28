package org.name.tool.core.metrics.projectlevel.cc;

import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.ProjectAnalyzerResults;
import org.name.tool.core.results.SecurityMetricResult;

public class CCImpl extends CC {

    @Override
    public SecurityMetricResult<Integer> compute(ProjectAnalyzerResults projectResults) {
        int value = 0;
        for (ClassifiedAnalyzerResults classResults : projectResults) {
            if (classResults.isCritical()) {
                value++;
            }
        }
        return new SecurityMetricResult<>(getName(), getCode(), value);
    }
}
