package org.surface.surface.core.metrics;

import org.surface.surface.core.metrics.api.Metric;

import java.util.List;

interface MetricsFactory<T extends Metric<?, ?>> {
    List<T> getMetrics(List<String> metricCodes);
}
