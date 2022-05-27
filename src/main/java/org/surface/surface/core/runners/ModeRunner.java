package org.surface.surface.core.runners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.out.exporters.ResultsExporter;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public abstract class ModeRunner<T> {
    private static final Logger LOGGER = LogManager.getLogger();

    private final List<String> metrics;
    private final String target;
    private final Path outFilePath;
    private final String filesRegex;
    private ResultsExporter<T> resultsExporter;

    ModeRunner(List<String> metrics, String target, Path outFilePath, String filesRegex) {
        this.metrics = Objects.requireNonNull(metrics);
        this.target = Objects.requireNonNull(target);
        this.outFilePath = Objects.requireNonNull(outFilePath);
        this.filesRegex = filesRegex;
    }

    public List<String> getMetrics() {
        return metrics;
    }

    String getTarget() {
        return target;
    }

    Path getTargetPath() {
        return Paths.get(target).toAbsolutePath();
    }

    Path getOutFilePath() {
        return outFilePath;
    }

    String getFilesRegex() {
        return filesRegex;
    }

    void setResultsExporter(ResultsExporter<T> resultsExporter) {
        this.resultsExporter = resultsExporter;
    }

    void exportResults(T projectMetricsResults) throws IOException {
        LOGGER.info("* Exporting results to {}", getOutFilePath());
        try {
            resultsExporter.export(projectMetricsResults);
            LOGGER.info("* Exporting completed to {}", getOutFilePath());
        } catch (IOException e) {
            LOGGER.info("* Exporting failed to {}", getOutFilePath());
            throw e;
        }
    }

    public abstract void run() throws Exception;
}
