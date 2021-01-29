package org.name.tool.core.metrics.classlevel.rp;

import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.MetricResult;

public class RPImpl extends RP {

    @Override
    public MetricResult<Boolean> compute(ClassifiedAnalyzerResults classResults) {
        boolean value = classResults.isUsingReflection();
        return new MetricResult<>(getName(), getCode(), value);
    }
}
