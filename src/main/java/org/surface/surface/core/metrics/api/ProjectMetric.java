package org.surface.surface.core.metrics.api;

import org.surface.surface.results.ProjectAnalyzerResults;
import org.surface.surface.results.values.MetricValue;

public abstract class ProjectMetric<U extends MetricValue<?>> extends Metric<ProjectAnalyzerResults, U> {
    public abstract U compute(ProjectAnalyzerResults projectResults);
}
