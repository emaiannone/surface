package org.surface.surface.out.exporters;

import org.surface.surface.core.metrics.results.ProjectMetricsResults;
import org.surface.surface.out.writers.Writer;

import java.io.IOException;

public abstract class ResultsExporter {
    private Writer writer;

    public ResultsExporter(Writer writer) {
        this.writer = writer;
    }

    public Writer getResultsWriter() {
        return writer;
    }

    public void setResultsWriter(Writer writer) {
        this.writer = writer;
    }

    public abstract void export(ProjectMetricsResults projectMetricsResults) throws IOException;
}
