package org.name.tool.core.metrics.ca;

import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.SecurityMetricValue;

public class CAImpl extends CA {

    @Override
    public SecurityMetricValue compute(ClassifiedAnalyzerResults classResults) {
        double value = classResults.getClassifiedAttributes().size();
        return new SecurityMetricValue(getName(), getCode(), value);
    }
}
