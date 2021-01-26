package org.name.tool.core.metrics.cm;

import org.name.tool.core.analysis.results.ClassifiedAnalyzerResults;
import org.name.tool.core.metrics.api.SecurityMetricValue;

public class ClassifiedMethodsImpl extends ClassifiedMethods {

    @Override
    public SecurityMetricValue compute(ClassifiedAnalyzerResults classResults) {
        double value = classResults.getClassifiedMethods().size();
        return new SecurityMetricValue(this, value);
    }
}
