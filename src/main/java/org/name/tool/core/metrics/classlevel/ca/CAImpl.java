package org.name.tool.core.metrics.classlevel.ca;

import org.name.tool.results.ClassifiedAnalyzerResults;
import org.name.tool.results.values.IntMetricValue;

public class CAImpl extends CA {

    @Override
    public IntMetricValue compute(ClassifiedAnalyzerResults classResults) {
        int value = classResults.getClassifiedAttributes().size();
        return new IntMetricValue(getName(), getCode(), value);
    }
}
