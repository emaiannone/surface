package org.surface.surface.core.metrics.api;

import org.surface.surface.core.inspection.results.ClassInspectorResults;
import org.surface.surface.core.metrics.results.values.MetricValue;

public abstract class ClassMetric<U extends MetricValue<?>> extends Metric<ClassInspectorResults, U> {
    public abstract U compute(ClassInspectorResults classResults);
}
