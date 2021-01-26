package org.name.tool.core.metrics.ca;

import org.name.tool.core.analysis.results.ClassifiedAnalyzerResults;
import org.name.tool.core.metrics.api.SecurityMetricValue;

public class ClassifiedAttributesImpl extends ClassifiedAttributes {

    @Override
    public SecurityMetricValue compute(ClassifiedAnalyzerResults classResults) {
        double value = classResults.getClassifiedAttributes().size();
        return new SecurityMetricValue(this, value);
    }
}
