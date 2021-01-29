package org.name.tool.core.metrics.projectlevel.cc;

import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.MetricResult;
import org.name.tool.core.results.ProjectAnalyzerResults;

public class CCImpl extends CC {

    @Override
    public MetricResult<Integer> compute(ProjectAnalyzerResults projectResults) {
        int value = 0;
        for (ClassifiedAnalyzerResults classResults : projectResults) {
            if (classResults.isCritical()) {
                value++;
            }
        }
        return new MetricResult<>(getName(), getCode(), value);
    }
}
