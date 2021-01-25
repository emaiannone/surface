package org.name.tool.core.metrics.cm;

import org.name.tool.core.analysis.results.ClassifiedAnalyzerResults;
import org.name.tool.core.metrics.api.SecurityMetricResult;

public class ClassifiedMethodsImpl extends ClassifiedMethods {

    @Override
    public SecurityMetricResult compute(ClassifiedAnalyzerResults classResults) {
        double value = classResults.getClassifiedMethods().size();
        return new SecurityMetricResult(classResults, this, value);
    }
}
