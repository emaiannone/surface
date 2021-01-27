package org.name.tool.core.metrics.cm;

import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.SecurityMetricValue;

public class CMImpl extends CM {

    @Override
    public SecurityMetricValue compute(ClassifiedAnalyzerResults classResults) {
        double value = classResults.getClassifiedMethods().size();
        return new SecurityMetricValue(getName(), getCode(), value);
    }
}
