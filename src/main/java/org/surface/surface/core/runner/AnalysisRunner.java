package org.surface.surface.core.runner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.inspection.ProjectInspector;
import org.surface.surface.core.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.metrics.api.ProjectMetricsCalculator;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;
import org.surface.surface.data.exports.writer.ResultsWriter;
import org.surface.surface.data.exports.writer.WriterFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public abstract class AnalysisRunner {
    private static final Logger LOGGER = LogManager.getLogger();

    private final List<String> metrics;
    private final String target;
    private final Path outFilePath;
    private final String filesRegex;

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

    protected boolean exportProjectResults(ProjectMetricsResults projectMetricsResults, String extension) throws IOException {
        LOGGER.info("* Exporting metrics results");
        ResultsWriter exporter = new WriterFactory().getWriter(extension);
        boolean exportResult = exporter.export(projectMetricsResults, outFilePath);
        if (exportResult) {
            LOGGER.info("* Exporting completed");
        } else {
            LOGGER.info("* Exporting failed");
        }
        return exportResult;
    }

    public abstract void run() throws IOException;
}
