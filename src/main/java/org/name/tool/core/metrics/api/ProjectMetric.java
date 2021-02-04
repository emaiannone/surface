package org.name.tool.core.metrics.api;

import org.name.tool.results.ProjectAnalyzerResults;
import org.name.tool.results.values.MetricValue;

public abstract class ProjectMetric<U extends MetricValue<?>> extends Metric<ProjectAnalyzerResults, U> {
    public abstract U compute(ProjectAnalyzerResults projectResults);
}
