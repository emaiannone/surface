package org.name.tool.core.metrics.api;

import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.SecurityMetricResult;

import java.io.Serializable;

public abstract class SecurityMetric<T extends Serializable> {
    private String name;
    private String code;

    public abstract SecurityMetricResult<T> compute(ClassifiedAnalyzerResults classResults);

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
