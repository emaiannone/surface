package org.surface.surface.core.metrics.api;

import org.surface.surface.core.inspection.results.InspectorResults;
import org.surface.surface.core.metrics.results.values.MetricValue;

// T is for the parameters type (AnalyzerResults implementor) and U is for the type of the MetricResult returned
public abstract class Metric<T extends InspectorResults, U extends MetricValue<?>> {
    private String name;
    private String code;

    public abstract U compute(T classResults);

    protected void setName(String name) {
        this.name = name;
    }

    protected String getName() {
        return name;
    }

    protected void setCode(String code) {
        this.code = code;
    }

    protected String getCode() {
        return code;
    }

    public String toString() {
        return name + " (" + code + ")";
    }
}
