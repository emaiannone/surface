package org.surface.surface.core.out.exporters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.out.writers.FileWriter;

import java.io.IOException;
import java.util.Map;

public abstract class ResultsExporter<T> {
    private static final Logger LOGGER = LogManager.getLogger();

    public void exportToFile(T projectMetricsResults, FileWriter writer) throws IOException {
        Map<String, Object> export = export(projectMetricsResults);
        LOGGER.trace("Results exported: {}", export);
        writer.write(export);
    }

    public abstract Map<String, Object> export(T projectMetricsResults);
}
