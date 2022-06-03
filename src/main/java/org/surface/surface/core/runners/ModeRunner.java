package org.surface.surface.core.runners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.metrics.api.MetricsManager;
import org.surface.surface.core.out.exporters.ResultsExporter;
import org.surface.surface.core.out.writers.FileWriter;

import java.io.IOException;

public abstract class ModeRunner<T> {
    private static final Logger LOGGER = LogManager.getLogger();

    private final MetricsManager metricsManager;
    private final FileWriter writer;
    private final String filesRegex;
    private ResultsExporter<T> resultsExporter;
    private String codeName;

    ModeRunner(MetricsManager metricsManager, FileWriter writer, String filesRegex) {
        this.metricsManager = metricsManager;
        this.writer = writer;
        this.filesRegex = filesRegex;
    }

    public MetricsManager getMetricsManager() {
        return metricsManager;
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
            resultsExporter.exportToFile(projectMetricsResults, writer);
            LOGGER.info("* Exporting completed to {}", writer.getOutFile());
        } catch (IOException e) {
            LOGGER.info("* Exporting failed to {}", writer.getOutFile());
            throw e;
        }
    }

    public abstract void run() throws Exception;
}
