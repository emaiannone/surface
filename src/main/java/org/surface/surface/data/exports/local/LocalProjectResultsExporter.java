package org.surface.surface.data.exports.local;

import org.surface.surface.results.ProjectMetricsResults;

import java.io.IOException;

public class LocalProjectResultsExporter {
    private final ProjectMetricsResults projectMetricsResults;

    public LocalProjectResultsExporter(ProjectMetricsResults projectMetricsResults) {
        this.projectMetricsResults = projectMetricsResults;
    }

    public boolean export(String exportFormat, String outFile) throws IOException {
        ResultsExporter exporter = new ExporterFactory().getExporter(exportFormat);
        return exporter.export(projectMetricsResults, outFile);
    }
}
