package org.name.tool.core.metrics.api;

import org.name.tool.core.results.AnalyzerResults;
import org.name.tool.core.results.SecurityMetricResult;

import java.io.Serializable;

// T is for the parameters type (AnalyzerResults implementor) and U is for the type of the SecurityMetricResult returned
public abstract class SecurityMetric<T extends AnalyzerResults, U extends Serializable> {
    private String name;
    private String code;

    public abstract SecurityMetricResult<U> compute(T classResults);

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
