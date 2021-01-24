package org.name.tool.core.metrics.ca;

import org.name.tool.core.analysis.results.ClassifiedAnalyzerResults;
import org.name.tool.core.metrics.api.SecurityMetricResult;

public class ClassifiedAttributesImpl implements ClassifiedAttributes {

    @Override
    public SecurityMetricResult compute(ClassifiedAnalyzerResults classResults) {
        double value = classResults.getClassifiedAttributes().size();
        return new SecurityMetricResult(classResults, value, NAME);
    }
}
