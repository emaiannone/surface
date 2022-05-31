package org.surface.surface.core.out.exporters;

import org.surface.surface.core.out.writers.FileWriter;

import java.io.IOException;

public abstract class ResultsExporter<T> {
    public abstract void export(T projectMetricsResults, FileWriter writer) throws IOException;
}
