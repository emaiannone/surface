package org.name.tool.core.metrics.classlevel.rp;

import org.name.tool.results.ClassifiedAnalyzerResults;
import org.name.tool.results.values.BooleanMetricValue;

public class RPImpl extends RP {

    @Override
    public BooleanMetricValue compute(ClassifiedAnalyzerResults classResults) {
        boolean value = classResults.isUsingReflection();
        return new BooleanMetricValue(getName(), getCode(), value);
    }
}
