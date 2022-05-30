package org.surface.surface.core.runners;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.common.RunSetting;
import org.surface.surface.core.out.exporters.ResultsExporter;

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
    private final String outFileExtension;
    private final String defaultFilesRegex;
    private ResultsExporter<T> resultsExporter;

    ModeRunner(List<String> metrics, String target, Pair<String, String> outFile, String defaultFilesRegex) {
        this.metrics = Objects.requireNonNull(metrics);
        this.target = Objects.requireNonNull(target);
        this.outFilePath = Objects.requireNonNull(Paths.get(outFile.getKey()).toAbsolutePath());
        this.outFileExtension = Objects.requireNonNull(outFile.getValue());
        this.defaultFilesRegex = defaultFilesRegex;
    }

    public static ModeRunner<?> newModeRunner(RunSetting runSetting) {
        return ModeRunnerFactory.newModeRunner(runSetting);
    }

    public List<String> getMetrics() {
        return metrics;
    }

    String getTarget() {
        return target;
    }

    Path getOutFilePath() {
        return outFilePath;
    }

    public String getOutFileExtension() {
        return outFileExtension;
    }

    String getDefaultFilesRegex() {
        return defaultFilesRegex;
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
