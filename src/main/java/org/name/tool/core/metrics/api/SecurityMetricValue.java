package org.name.tool.core.metrics.api;

public class SecurityMetricValue {
    private final SecurityMetric metric;
    private final double value;

    public SecurityMetricValue(SecurityMetric metric, double value) {
        this.metric = metric;
        this.value = value;
    }

    public SecurityMetric getMetric() {
        return metric;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "(" + metric + ", " + value + ")";
    }
}
