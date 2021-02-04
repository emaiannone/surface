package org.name.tool.core.metrics.api;

import org.name.tool.results.ClassifiedAnalyzerResults;
import org.name.tool.results.MetricValue;

public abstract class ClassMetric<U> extends Metric<ClassifiedAnalyzerResults, U> {
    public abstract MetricValue<U> compute(ClassifiedAnalyzerResults classResults);
}
