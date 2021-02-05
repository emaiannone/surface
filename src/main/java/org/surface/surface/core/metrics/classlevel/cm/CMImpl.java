package org.surface.surface.core.metrics.classlevel.cm;

import org.surface.surface.results.ClassifiedAnalyzerResults;
import org.surface.surface.results.values.IntMetricValue;

public class CMImpl extends CM {

    @Override
    public IntMetricValue compute(ClassifiedAnalyzerResults classResults) {
        int value = classResults.getClassifiedMethods().size();
        return new IntMetricValue(getName(), getCode(), value);
    }
}
