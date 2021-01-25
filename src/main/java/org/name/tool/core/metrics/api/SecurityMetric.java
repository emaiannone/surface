package org.name.tool.core.metrics.api;

import org.name.tool.core.analysis.results.ClassifiedAnalyzerResults;

public abstract class SecurityMetric {
    private String name;
    private String code;

    public abstract SecurityMetricResult compute(ClassifiedAnalyzerResults classResults);

    protected void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    protected void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String toString() {
        return name + " (" + code + ")";
    }
}
