package org.name.tool.core.metrics.api;

import org.name.tool.results.MetricValue;
import org.name.tool.results.ProjectAnalyzerResults;

public abstract class ProjectMetric<U> extends Metric<ProjectAnalyzerResults, U> {
    public abstract MetricValue<U> compute(ProjectAnalyzerResults projectResults);
}
