package org.name.tool.export;

import org.name.tool.core.results.ProjectMetricsResults;

public interface Exporter {
    boolean export(ProjectMetricsResults projectMetricsResults);
}
