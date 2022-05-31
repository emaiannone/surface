package org.surface.surface.core.analysis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.explorers.JavaFilesExplorer;
import org.surface.surface.core.inspection.ProjectInspector;
import org.surface.surface.core.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.metrics.api.Metric;
import org.surface.surface.core.metrics.api.ProjectMetricsCalculator;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class SnapshotAnalyzer {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Path projectDirPath;
    private final String filesRegex;
    private final List<Metric<?,?>> metrics;

    public SnapshotAnalyzer(Path projectDirPath, String filesRegex, List<Metric<?,?>> metrics) {
        this.projectDirPath = projectDirPath;
        this.filesRegex = filesRegex;
        this.metrics = metrics;
    }

    public ProjectMetricsResults analyze() throws IOException {
        List<Path> allowedFiles = JavaFilesExplorer.selectFiles(projectDirPath, filesRegex);
        LOGGER.debug("Java files found: {}", allowedFiles);
        LOGGER.debug("Going to inspect {} files in {}", allowedFiles.size(), projectDirPath);
        ProjectInspector projectInspector = new ProjectInspector(projectDirPath, allowedFiles);
        ProjectInspectorResults projectInspectorResults = projectInspector.inspect();
        ProjectMetricsCalculator projectMetricsCalculator = new ProjectMetricsCalculator(projectInspectorResults);
        LOGGER.debug("* Metrics computation started");
        ProjectMetricsResults projectMetricsResults = projectMetricsCalculator.calculate(metrics);
        LOGGER.debug("* Metrics computation ended");
        LOGGER.trace("Metrics results:\n\t{}", projectMetricsResults.toString().replaceAll("\n", "\n\t"));
        return projectMetricsResults;
    }
}
