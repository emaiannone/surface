package org.surface.surface.core.engine.metrics.results.values;

abstract class NumericMetricValue<T extends Number> extends MetricValue<T> {
    NumericMetricValue(String metricName, String metricCode, T value) {
        super(metricName, metricCode, value);
    }
}
