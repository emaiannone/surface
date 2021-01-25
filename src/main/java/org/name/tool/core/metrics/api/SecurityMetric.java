package org.name.tool.core.metrics.api;

import org.name.tool.core.analysis.results.ClassifiedAnalyzerResults;

public interface SecurityMetric {
    SecurityMetricResult compute(ClassifiedAnalyzerResults classResults);
    String getName();
}
