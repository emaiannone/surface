package org.name.tool.core.metrics.classlevel.rp;

import org.name.tool.results.ClassifiedAnalyzerResults;
import org.name.tool.results.MetricValue;

public class RPImpl extends RP {

    @Override
    public MetricValue<Boolean> compute(ClassifiedAnalyzerResults classResults) {
        boolean value = classResults.isUsingReflection();
        return new MetricValue<>(getName(), getCode(), value);
    }
}
