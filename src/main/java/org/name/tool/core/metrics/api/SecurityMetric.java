package org.name.tool.core.metrics.api;

import org.name.tool.core.analysis.results.ClassifiedAnalyzerResults;

public interface SecurityMetric {
    String CODE = "";
    String NAME = "";
    SecurityMetricResult compute(ClassifiedAnalyzerResults classResults);
}
