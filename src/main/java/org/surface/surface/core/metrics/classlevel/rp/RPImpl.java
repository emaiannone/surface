package org.surface.surface.core.metrics.classlevel.rp;

import org.surface.surface.results.ClassifiedAnalyzerResults;
import org.surface.surface.results.values.BooleanMetricValue;

public class RPImpl extends RP {

    @Override
    public BooleanMetricValue compute(ClassifiedAnalyzerResults classResults) {
        boolean value = classResults.isUsingReflection();
        return new BooleanMetricValue(getName(), getCode(), value);
    }
}
