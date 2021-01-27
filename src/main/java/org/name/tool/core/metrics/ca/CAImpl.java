package org.name.tool.core.metrics.ca;

import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.SecurityMetricResult;

public class CAImpl extends CA {

    @Override
    public SecurityMetricResult<Integer> compute(ClassifiedAnalyzerResults classResults) {
        int value = classResults.getClassifiedAttributes().size();
        return new SecurityMetricResult<>(getName(), getCode(), value);
    }
}
