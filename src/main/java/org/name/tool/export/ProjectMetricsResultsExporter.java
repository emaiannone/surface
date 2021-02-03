package org.name.tool.export;

import org.name.tool.core.results.ProjectMetricsResults;

public class ProjectMetricsResultsExporter {
    private final ProjectMetricsResults projectMetricsResults;

    public ProjectMetricsResultsExporter(ProjectMetricsResults projectMetricsResults) {
        this.projectMetricsResults = projectMetricsResults;
    }

    public boolean exportAs(String exportFormat) {
        Exporter exporter = new ExporterFactory().getExporter(exportFormat);
        return exporter.export(projectMetricsResults);
    }
}
