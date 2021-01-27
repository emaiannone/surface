package org.name.tool.core.metrics.cm;

import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.SecurityMetricValue;

public class CMImpl extends CM {

    @Override
    public SecurityMetricValue<Integer> compute(ClassifiedAnalyzerResults classResults) {
        int value = classResults.getClassifiedMethods().size();
        return new SecurityMetricValue<>(getName(), getCode(), value);
    }
}
