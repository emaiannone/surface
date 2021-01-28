package org.name.tool.core.metrics.cc;

import org.name.tool.core.metrics.ca.CA;
import org.name.tool.core.metrics.cm.CM;
import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.SecurityMetricResult;

public class CCImpl extends CC {
    private final CA ca;
    private final CM cm;

    public CCImpl(CA ca, CM cm) {
        this.ca = ca;
        this.cm = cm;
    }

    @Override
    public SecurityMetricResult<Boolean> compute(ClassifiedAnalyzerResults classResults) {
        boolean value = ca.compute(classResults).getValue() + cm.compute(classResults).getValue() > 0;
        return new SecurityMetricResult<>(getName(), getCode(), value);
    }
}
