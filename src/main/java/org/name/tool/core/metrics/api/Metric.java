package org.name.tool.core.metrics.api;

import org.name.tool.results.AnalyzerResults;
import org.name.tool.results.values.MetricValue;

// T is for the parameters type (AnalyzerResults implementor) and U is for the type of the MetricResult returned
public abstract class Metric<T extends AnalyzerResults, U extends MetricValue<?>> {
    private String name;
    private String code;

    public abstract U compute(T classResults);

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
