package org.surface.surface.results.values;

public abstract class MetricValue<T> {
    private final String metricName;
    private final String metricCode;
    private final T value;

    public MetricValue(String metricName, String metricCode, T value) {
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

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "(" + metricName + ", " + value + ")";
    }
}
