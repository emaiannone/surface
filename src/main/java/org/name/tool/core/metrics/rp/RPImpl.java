package org.name.tool.core.metrics.rp;

import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.SecurityMetricValue;

public class RPImpl extends RP {

    @Override
    public SecurityMetricValue<Boolean> compute(ClassifiedAnalyzerResults classResults) {
        boolean value = classResults.isUsingReflection();
        return new SecurityMetricValue<>(getName(), getCode(), value);
    }
}
