package org.surface.surface.core.metrics.classlevel.ca;

import org.surface.surface.results.ClassifiedAnalyzerResults;
import org.surface.surface.results.values.IntMetricValue;

public class CAImpl extends CA {

    @Override
    public IntMetricValue compute(ClassifiedAnalyzerResults classResults) {
        int value = classResults.getClassifiedAttributes().size();
        return new IntMetricValue(getName(), getCode(), value);
    }
}
