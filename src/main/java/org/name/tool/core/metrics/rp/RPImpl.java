package org.name.tool.core.metrics.rp;

import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.SecurityMetricResult;

public class RPImpl extends RP {

    @Override
    public SecurityMetricResult<Boolean> compute(ClassifiedAnalyzerResults classResults) {
        boolean value = classResults.isUsingReflection();
        return new SecurityMetricResult<>(getName(), getCode(), value);
    }
}
