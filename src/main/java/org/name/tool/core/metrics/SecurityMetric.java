package org.name.tool.core.metrics;

import org.name.tool.core.analysis.results.ClassifiedAnalyzerResults;

public interface SecurityMetric {
    double compute(ClassifiedAnalyzerResults classMap);
}
