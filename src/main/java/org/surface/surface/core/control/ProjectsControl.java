package org.surface.surface.core.control;

import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import org.surface.surface.core.analysis.ProjectAnalyzer;
import org.surface.surface.core.metrics.api.ProjectMetricsCalculator;
import org.surface.surface.results.ProjectAnalyzerResults;
import org.surface.surface.results.ProjectMetricsResults;

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

    protected ProjectMetricsResults processProject(Path projectAbsolutePath) {
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

        // Release! Sadly, the library does not manage well its internal cache, so we have to do this manual clear
        JavaParserFacade.clearInstances();

        return projectMetricsResults;
    }

    public abstract void run();
}
