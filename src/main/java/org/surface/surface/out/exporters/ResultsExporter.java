package org.surface.surface.out.exporters;

import org.surface.surface.out.writers.Writer;

import java.io.IOException;

public abstract class ResultsExporter<T> {
    private Writer writer;

    ResultsExporter(Writer writer) {
        this.writer = writer;
    }

    Writer getResultsWriter() {
        return writer;
    }

    public void setResultsWriter(Writer writer) {
        this.writer = writer;
    }

    public abstract void export(T projectMetricsResults) throws IOException;
}
