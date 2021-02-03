package org.name.tool.core.metrics.classlevel.ca;

import org.name.tool.results.ClassifiedAnalyzerResults;
import org.name.tool.results.MetricResult;

public class CAImpl extends CA {

    @Override
    public MetricResult<Integer> compute(ClassifiedAnalyzerResults classResults) {
        int value = classResults.getClassifiedAttributes().size();
        return new MetricResult<>(getName(), getCode(), value);
    }
}
