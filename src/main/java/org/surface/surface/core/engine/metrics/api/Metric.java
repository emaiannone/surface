package org.surface.surface.core.engine.metrics.api;

import org.surface.surface.core.engine.inspection.results.InspectorResults;
import org.surface.surface.core.engine.metrics.results.values.MetricValue;

// T is for the parameters type (AnalyzerResults implementor) and U is for the type of the MetricResult returned
public abstract class Metric<T extends InspectorResults, U extends MetricValue<?>> {
    private String name;
    private String code;

    public abstract U compute(T inspectorResult);

    public String toString() {
        return name + " (" + code + ")";
    }

    protected double computeRatio(int num, int denom) {
        return denom != 0 ? (double) num / denom : 0.0;
    }

    protected String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected String getCode() {
        return code;
    }

    protected void setCode(String code) {
        this.code = code;
    }
}
