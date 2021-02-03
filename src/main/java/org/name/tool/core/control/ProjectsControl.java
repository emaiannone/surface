package org.name.tool.core.control;

import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import org.name.tool.core.analysis.ProjectAnalyzer;
import org.name.tool.core.metrics.api.ProjectMetricsCalculator;
import org.name.tool.core.results.ProjectAnalyzerResults;
import org.name.tool.core.results.ProjectMetricsResults;

import java.nio.file.Path;

public abstract class ProjectsControl {
    private final String[] metricsCodes;
    private final String exportFormat;

    public ProjectsControl(String[] metricsCodes, String exportFormat) {
        this.metricsCodes = metricsCodes;
        this.exportFormat = exportFormat;
    }

    public String[] getMetricsCodes() {
        return metricsCodes.clone();
    }

    public String getExportFormat() {
        return exportFormat;
    }

    protected void processProject(Path projectAbsolutePath) {
        ProjectAnalyzer projectAnalyzer = new ProjectAnalyzer(projectAbsolutePath);
        System.out.println("* Project Analysis starting");
        ProjectAnalyzerResults projectAnalyzerResults = projectAnalyzer.analyze();
        System.out.println("* Project Analysis finished");
        System.out.println("* Printing Project results");
        System.out.println(projectAnalyzerResults);

        System.out.println("* Project Metrics Computation starting");
        ProjectMetricsCalculator projectMetricsCalculator = new ProjectMetricsCalculator(projectAnalyzerResults);
        ProjectMetricsResults projectMetricsResults = projectMetricsCalculator.calculate(getMetricsCodes());
        System.out.println("* Project Metrics Computation finished");
        System.out.println("* Printing Project Metrics");
        System.out.println(projectMetricsResults);

        // TODO Implement Export
        System.out.println("* NOT IMPLEMENTED: Export results.");

        // Release! Sadly, the library does not manage well its internal cache, so we have to do this manual clear
        JavaParserFacade.clearInstances();
    }

    public abstract void run();
}
