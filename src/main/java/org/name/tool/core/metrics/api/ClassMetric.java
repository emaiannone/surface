package org.name.tool.core.metrics.api;

import org.name.tool.results.ClassifiedAnalyzerResults;
import org.name.tool.results.values.MetricValue;

public abstract class ClassMetric<U extends MetricValue<?>> extends Metric<ClassifiedAnalyzerResults, U> {
    public abstract U compute(ClassifiedAnalyzerResults classResults);
}
