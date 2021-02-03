package org.name.tool.export;

import org.name.tool.core.results.ProjectMetricsResults;

public class NullExporter implements Exporter {
    public static final String CODE = "";

    @Override
    public boolean export(ProjectMetricsResults projectMetricsResults) {
        return true;
    }
}
