package org.name.tool.core.metrics.api;

import org.name.tool.core.metrics.ClassSecurityMetricsFactory;
import org.name.tool.core.metrics.ProjectSecurityMetricsFactory;
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
            List<ClassSecurityMetric<?>> classMetrics = new ClassSecurityMetricsFactory().getSecurityMetrics(metricsCode);
            for (ClassSecurityMetric<?> classMetric : classMetrics) {
                classMetricsResults.add(classMetric.compute(classResults));
            }
            projectMetricsResults.add(classMetricsResults);
        }
        // Project-level metrics
        List<ProjectSecurityMetric<?>> projectMetrics = new ProjectSecurityMetricsFactory().getSecurityMetrics(metricsCode);
        for (ProjectSecurityMetric<?> projectMetric : projectMetrics) {
            projectMetricsResults.add(projectMetric.compute(projectAnalyzerResults));
        }
        return projectMetricsResults;
    }
}
