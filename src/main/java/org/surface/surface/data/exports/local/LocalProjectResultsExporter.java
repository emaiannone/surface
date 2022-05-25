package org.surface.surface.data.exports.local;

import org.surface.surface.core.metrics.results.ProjectMetricsResults;
import org.surface.surface.data.exports.writer.ResultsWriter;
import org.surface.surface.data.exports.writer.WriterFactory;

import java.io.IOException;
import java.nio.file.Path;

public class LocalProjectResultsExporter {
    private final ProjectMetricsResults projectMetricsResults;

    public LocalProjectResultsExporter(ProjectMetricsResults projectMetricsResults) {
        this.projectMetricsResults = projectMetricsResults;
    }

    public boolean export(String exportFormat, Path outFilePath) throws IOException {
        ResultsWriter exporter = new WriterFactory().getWriter(exportFormat);
        return exporter.export(projectMetricsResults, outFilePath);
    }
}
