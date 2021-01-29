package org.name.tool.core.metrics.api;

import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.SecurityMetricResult;

public abstract class ClassSecurityMetric<U> extends SecurityMetric<ClassifiedAnalyzerResults, U> {
    public abstract SecurityMetricResult<U> compute(ClassifiedAnalyzerResults classResults);
}
