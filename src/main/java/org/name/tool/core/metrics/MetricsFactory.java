package org.name.tool.core.metrics;

import org.name.tool.core.metrics.api.Metric;

import java.util.List;

public interface MetricsFactory<T extends Metric<?, ?>> {
    List<T> getMetrics(String[] metricsCodes);
}
