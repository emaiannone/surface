package org.name.tool.core.metrics.classlevel.ca;

import org.name.tool.results.ClassifiedAnalyzerResults;
import org.name.tool.results.MetricValue;

public class CAImpl extends CA {

    @Override
    public MetricValue<Integer> compute(ClassifiedAnalyzerResults classResults) {
        int value = classResults.getClassifiedAttributes().size();
        return new MetricValue<>(getName(), getCode(), value);
    }
}
