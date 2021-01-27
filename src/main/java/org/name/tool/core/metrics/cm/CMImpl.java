package org.name.tool.core.metrics.cm;

import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.SecurityMetricResult;

public class CMImpl extends CM {

    @Override
    public SecurityMetricResult<Integer> compute(ClassifiedAnalyzerResults classResults) {
        int value = classResults.getClassifiedMethods().size();
        return new SecurityMetricResult<>(getName(), getCode(), value);
    }
}
