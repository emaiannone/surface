package org.name.tool.core.metrics.classlevel.cm;

import org.name.tool.results.ClassifiedAnalyzerResults;
import org.name.tool.results.values.IntMetricValue;

public class CMImpl extends CM {

    @Override
    public IntMetricValue compute(ClassifiedAnalyzerResults classResults) {
        int value = classResults.getClassifiedMethods().size();
        return new IntMetricValue(getName(), getCode(), value);
    }
}
