package org.name.tool.core.metrics.api;

import org.name.tool.core.metrics.ClassMetricsFactory;
import org.name.tool.core.metrics.ProjectMetricsFactory;
import org.name.tool.core.results.ClassMetricsResults;
import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.ProjectAnalyzerResults;
import org.name.tool.core.results.ProjectMetricsResults;

import java.util.List;

public class ProjectMetricsCalculator {
    private final ProjectAnalyzerResults projectAnalyzerResults;

    public ProjectMetricsCalculator(ProjectAnalyzerResults projectAnalyzerResults) {
        this.projectAnalyzerResults = projectAnalyzerResults;
    }

    public ProjectMetricsResults calculate(String[] metricsCode) {
        ProjectMetricsResults projectMetricsResults = new ProjectMetricsResults(projectAnalyzerResults.getProjectRoot());
        // Class-level metrics
        for (ClassifiedAnalyzerResults classResults : projectAnalyzerResults) {
            ClassMetricsResults classMetricsResults = new ClassMetricsResults(classResults);
            List<ClassMetric<?>> classMetrics = new ClassMetricsFactory().getMetrics(metricsCode);
            for (ClassMetric<?> classMetric : classMetrics) {
                classMetricsResults.add(classMetric.compute(classResults));
            }
            projectMetricsResults.add(classMetricsResults);
        }
        // Project-level metrics
        List<ProjectMetric<?>> projectMetrics = new ProjectMetricsFactory().getMetrics(metricsCode);
        for (ProjectMetric<?> projectMetric : projectMetrics) {
            projectMetricsResults.add(projectMetric.compute(projectAnalyzerResults));
        }
        return projectMetricsResults;
    }
}
