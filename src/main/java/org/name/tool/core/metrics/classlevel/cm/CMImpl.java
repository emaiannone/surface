package org.name.tool.core.metrics.classlevel.cm;

import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.MetricResult;

public class CMImpl extends CM {

    @Override
    public MetricResult<Integer> compute(ClassifiedAnalyzerResults classResults) {
        int value = classResults.getClassifiedMethods().size();
        return new MetricResult<>(getName(), getCode(), value);
    }
}
