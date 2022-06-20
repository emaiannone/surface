package org.surface.surface.core.engine.metrics.api;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.MetricValue;

public abstract class ClassMetric<U extends MetricValue<?>> extends Metric<ClassInspectorResults, U> {
    public abstract U compute(ClassInspectorResults classResults);
}
