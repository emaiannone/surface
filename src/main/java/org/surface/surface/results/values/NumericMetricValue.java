package org.surface.surface.results.values;

public abstract class NumericMetricValue<T extends Number> extends MetricValue<T> {
    public NumericMetricValue(String metricName, String metricCode, T value) {
        super(metricName, metricCode, value);
    }
}
