package org.name.tool.core.metrics.api;

import org.name.tool.core.results.ProjectAnalyzerResults;
import org.name.tool.core.results.SecurityMetricResult;

import java.io.Serializable;

public abstract class ProjectSecurityMetric<U extends Serializable> extends SecurityMetric<ProjectAnalyzerResults, U> {
    public abstract SecurityMetricResult<U> compute(ProjectAnalyzerResults projectResults);
}
