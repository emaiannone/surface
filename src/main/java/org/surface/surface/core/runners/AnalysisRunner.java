package org.surface.surface.core.runners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.inspection.ProjectInspector;
import org.surface.surface.core.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.metrics.api.ProjectMetricsCalculator;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;
import org.surface.surface.out.exporters.ResultsExporter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public abstract class AnalysisRunner<T> {
    private static final Logger LOGGER = LogManager.getLogger();

    private final List<String> metrics;
    private final String target;
    private final Path outFilePath;
    private final String filesRegex;
    private ResultsExporter<T> resultsExporter;

    public AnalysisRunner(List<String> metrics, String target, Path outFilePath, String filesRegex) {
        this.metrics = Objects.requireNonNull(metrics);
        this.target = Objects.requireNonNull(target);
        this.outFilePath = Objects.requireNonNull(outFilePath);
        this.filesRegex = filesRegex;
    }

    public List<String> getMetrics() {
        return metrics;
    }

    public String getTarget() {
        return target;
    }

    public Path getOutFilePath() {
        return outFilePath;
    }

    public String getFilesRegex() {
        return filesRegex;
    }

    public void setResultsExporter(ResultsExporter<T> resultsExporter) {
        this.resultsExporter = resultsExporter;
    }

    protected ProjectMetricsResults analyze(Path targetDir, List<Path> allowedFiles) throws IOException {
        LOGGER.info("* Inspecting {} files in {}", allowedFiles.size(), targetDir);
        ProjectInspector projectInspector = new ProjectInspector(targetDir, allowedFiles);
        ProjectInspectorResults projectInspectorResults = projectInspector.inspect();
        ProjectMetricsCalculator projectMetricsCalculator = new ProjectMetricsCalculator(projectInspectorResults);
        LOGGER.info("* Metrics computation started");
        ProjectMetricsResults projectMetricsResults = projectMetricsCalculator.calculate(metrics);
        LOGGER.info("* Metrics computation ended");
        LOGGER.debug("Metrics results:\n\t{}", projectMetricsResults.toString().replaceAll("\n", "\n\t"));
        return projectMetricsResults;
    }

    protected void exportResults(T projectMetricsResults) throws IOException {
        LOGGER.info("* Exporting metrics results");
        try {
            resultsExporter.export(projectMetricsResults);
            LOGGER.info("* Exporting completed");
        } catch (IOException e) {
            LOGGER.info("* Exporting failed");
            throw e;
        }
    }

    public abstract void run() throws IOException;
}
