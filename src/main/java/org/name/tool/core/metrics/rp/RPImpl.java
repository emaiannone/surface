package org.name.tool.core.metrics.rp;

import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.SecurityMetricValue;

public class RPImpl extends RP {

    @Override
    public SecurityMetricValue compute(ClassifiedAnalyzerResults classResults) {
        double value = classResults.isUsingReflection() ? 1 : 0;
        return new SecurityMetricValue(getName(), getCode(), value);
    }
}
