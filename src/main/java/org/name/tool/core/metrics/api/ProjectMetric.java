package org.name.tool.core.metrics.api;

import org.name.tool.core.results.MetricResult;
import org.name.tool.core.results.ProjectAnalyzerResults;

public abstract class ProjectMetric<U> extends Metric<ProjectAnalyzerResults, U> {
    public abstract MetricResult<U> compute(ProjectAnalyzerResults projectResults);
}
