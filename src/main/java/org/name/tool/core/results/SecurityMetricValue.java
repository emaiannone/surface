package org.name.tool.core.results;

public class SecurityMetricValue {
    private final String metricName;
    private final String metricCode;
    private final double value;

    public SecurityMetricValue(String metricName, String metricCode, double value) {
        this.metricName = metricName;
        this.metricCode = metricCode;
        this.value = value;
    }

    public String getMetricName() {
        return metricName;
    }

    public String getMetricCode() {
        return metricCode;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "(" + metricName + ", " + value + ")";
    }
}
