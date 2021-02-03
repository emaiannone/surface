package org.name.tool.core.metrics.api;

import org.name.tool.results.ClassifiedAnalyzerResults;
import org.name.tool.results.MetricResult;

public abstract class ClassMetric<U> extends Metric<ClassifiedAnalyzerResults, U> {
    public abstract MetricResult<U> compute(ClassifiedAnalyzerResults classResults);
}
