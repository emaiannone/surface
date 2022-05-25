package org.surface.surface.core.metrics.api;

import org.surface.surface.core.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.metrics.results.values.MetricValue;

public abstract class ProjectMetric<U extends MetricValue<?>> extends Metric<ProjectInspectorResults, U> {
    public abstract U compute(ProjectInspectorResults projectResults);
}
