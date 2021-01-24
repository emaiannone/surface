package org.name.tool.core.metrics.api;

import org.name.tool.core.analysis.results.ClassifiedAnalyzerResults;

public class SecurityMetricResult {
    private final ClassifiedAnalyzerResults classResults;
    private final double value;
    private final String name;

    public SecurityMetricResult(ClassifiedAnalyzerResults classResults, double value, String name) {
        this.classResults = classResults;
        this.value = value;
        this.name = name;
    }

    public ClassifiedAnalyzerResults getClassResults() {
        return classResults;
    }

    public double getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
