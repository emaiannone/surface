package org.name.tool.core.metrics.api;

import org.name.tool.core.analysis.results.ClassifiedAnalyzerResults;

public class SecurityMetricResult {
    private final ClassifiedAnalyzerResults classResults;
    private final SecurityMetric metric;
    private final double value;

    public SecurityMetricResult(ClassifiedAnalyzerResults classResults, SecurityMetric metric, double value) {
        this.classResults = classResults;
        this.metric = metric;
        this.value = value;
    }

    public ClassifiedAnalyzerResults getClassResults() {
        return classResults;
    }

    public SecurityMetric getMetric() {
        return metric;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "(" + classResults.getClassName() + ", " + metric + ", " + value + ")";
    }
}
