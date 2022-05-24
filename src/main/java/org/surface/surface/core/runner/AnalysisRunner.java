package org.surface.surface.core.runner;

import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.RunMode;
import org.surface.surface.core.analysis.ProjectAnalyzer;
import org.surface.surface.core.metrics.api.ProjectMetricsCalculator;
import org.surface.surface.results.ProjectAnalyzerResults;
import org.surface.surface.results.ProjectMetricsResults;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public abstract class AnalysisRunner {
    private static final Logger LOGGER = LogManager.getLogger();

    private final List<String> metrics;
    private final String target;
    private final File outFile;
    private final String filesRegex;

    public AnalysisRunner(List<String> metrics, String target, File outFile, String filesRegex) {
        this.metrics = Objects.requireNonNull(metrics);
        this.target = Objects.requireNonNull(target);
        this.outFile = Objects.requireNonNull(outFile);
        this.filesRegex = filesRegex;
    }

    public List<String> getMetrics() {
        return metrics;
    }

    public String getTarget() {
        return target;
    }

    public File getOutFile() {
        return outFile;
    }

    public String getFilesRegex() {
        return filesRegex;
    }

    protected ProjectMetricsResults analyzeProject(File projectDir, List<Path> files) {
        ProjectMetricsResults projectMetricsResults = null;
        LOGGER.info("Bye");
        System.exit(1);
        /* TODO Rework
        ProjectAnalyzer projectAnalyzer = new ProjectAnalyzer(projectDir);
        LOGGER.info("* Project Analysis starting");
        ProjectAnalyzerResults projectAnalyzerResults = projectAnalyzer.analyze();
        LOGGER.info("* Project Analysis finished");
        LOGGER.info("* Printing Project results");
        LOGGER.info(projectAnalyzerResults);

        LOGGER.info("* Project Metrics Computation starting");
        ProjectMetricsCalculator projectMetricsCalculator = new ProjectMetricsCalculator(projectAnalyzerResults);
        ProjectMetricsResults projectMetricsResults = projectMetricsCalculator.calculate(getMetricsCodes());
        LOGGER.info("* Project Metrics Computation finished");
        LOGGER.info("* Printing Project Metrics");
        LOGGER.info(projectMetricsResults);

        // Release! Sadly, the library does not manage well its internal cache, so we have to do this manual clear
        JavaParserFacade.clearInstances();
        */
        return projectMetricsResults;
    }

    protected void exportProjectResults(ProjectMetricsResults projectMetricsResults) {
        // TODO Export (just JSON)
    }

    public abstract void run() throws IOException;
}
