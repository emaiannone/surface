package org.surface.surface.core.runners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.metrics.api.Metric;
import org.surface.surface.core.out.exporters.ResultsExporter;
import org.surface.surface.core.out.writers.FileWriter;

import java.io.IOException;
import java.util.List;

public abstract class ModeRunner<T> {
    private static final Logger LOGGER = LogManager.getLogger();

    private final List<Metric<?, ?>> metrics;
    private final FileWriter writer;
    private final String filesRegex;
    private ResultsExporter<T> resultsExporter;
    private String codeName;

    ModeRunner(List<Metric<?, ?>> metrics, FileWriter writer, String filesRegex) {
        this.metrics = metrics;
        this.writer = writer;
        this.filesRegex = filesRegex;
    }

    public List<Metric<?, ?>> getMetrics() {
        return metrics;
    }

    public FileWriter getWriter() {
        return writer;
    }

    public String getFilesRegex() {
        return filesRegex;
    }

    public ResultsExporter<T> getResultsExporter() {
        return resultsExporter;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    void setResultsExporter(ResultsExporter<T> resultsExporter) {
        this.resultsExporter = resultsExporter;
    }

    void exportResults(T projectMetricsResults) throws IOException {
        LOGGER.info("* Exporting results to {}", writer.getOutFile());
        try {
            resultsExporter.export(projectMetricsResults, writer);
            LOGGER.info("* Exporting completed to {}", writer.getOutFile());
        } catch (IOException e) {
            LOGGER.info("* Exporting failed to {}", writer.getOutFile());
            throw e;
        }
    }

    public abstract void run() throws Exception;
}
