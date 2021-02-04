package org.name.tool.core.metrics.classlevel.cm;

import org.name.tool.results.ClassifiedAnalyzerResults;
import org.name.tool.results.MetricValue;

public class CMImpl extends CM {

    @Override
    public MetricValue<Integer> compute(ClassifiedAnalyzerResults classResults) {
        int value = classResults.getClassifiedMethods().size();
        return new MetricValue<>(getName(), getCode(), value);
    }
}
