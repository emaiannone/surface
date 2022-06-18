package org.surface.surface.core.configuration.runners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.configuration.runners.results.RunResults;
import org.surface.surface.core.engine.metrics.api.MetricsManager;
import org.surface.surface.core.engine.writers.FileWriter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class RunningMode<T extends RunResults> {
    private static final Logger LOGGER = LogManager.getLogger();

    private T runResults;
    private final MetricsManager metricsManager;
    private final FileWriter writer;
    private final String filesRegex;
    private final boolean includeTests;
    private String codeName;

    RunningMode(FileWriter writer, MetricsManager metricsManager, String filesRegex, boolean includeTests) {
        if (writer == null) {
            throw new IllegalArgumentException("The writer to an output file must not be null.");
        }
        this.metricsManager = metricsManager;
        this.writer = writer;
        this.filesRegex = filesRegex;
        this.includeTests = includeTests;
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

    public boolean isIncludeTests() {
        return includeTests;
    }

    public String getCodeName() {
        return codeName;
    }

    public T getRunResults() {
        return runResults;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public void setRunResults(T runResults) {
        this.runResults = runResults;
    }

    void exportResults(T runResults) throws IOException {
        LOGGER.debug("* Exporting results to {}", writer.getOutFile());
        try {
            List<Map<String, Object>> export = runResults.export();
            LOGGER.trace("Results exported: {}", export);
            writer.write(export);
            LOGGER.debug("* Exporting completed to {}", writer.getOutFile());
        } catch (IOException e) {
            throw new IOException("Failed to export to " + writer.getOutFile(), e);
        }
    }

    public abstract void run() throws Exception;
}
