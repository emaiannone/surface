package org.name.tool.core.metrics.api;

import org.name.tool.core.results.ProjectAnalyzerResults;
import org.name.tool.core.results.SecurityMetricResult;

public abstract class ProjectSecurityMetric<U> extends SecurityMetric<ProjectAnalyzerResults, U> {
    public abstract SecurityMetricResult<U> compute(ProjectAnalyzerResults projectResults);
}
