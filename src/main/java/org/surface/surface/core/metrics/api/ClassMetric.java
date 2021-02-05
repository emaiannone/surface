package org.surface.surface.core.metrics.api;

import org.surface.surface.results.ClassifiedAnalyzerResults;
import org.surface.surface.results.values.MetricValue;

public abstract class ClassMetric<U extends MetricValue<?>> extends Metric<ClassifiedAnalyzerResults, U> {
    public abstract U compute(ClassifiedAnalyzerResults classResults);
}
