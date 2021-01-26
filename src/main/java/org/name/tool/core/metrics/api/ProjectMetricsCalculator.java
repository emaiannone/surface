package org.name.tool.core.metrics.api;

import org.name.tool.core.analysis.results.ClassifiedAnalyzerResults;
import org.name.tool.core.analysis.results.ProjectAnalyzerResults;
import org.name.tool.core.metrics.SecurityMetricsFactory;
import org.name.tool.core.metrics.api.results.ClassMetricsResults;
import org.name.tool.core.metrics.api.results.ProjectMetricsResults;

import java.util.List;

//TODO Move ALL *Results classes in a separate package, at the level of metrics and analysis?
public class ProjectMetricsCalculator {
    private final ProjectAnalyzerResults projectAnalyzerResults;

    public ProjectMetricsCalculator(ProjectAnalyzerResults projectAnalyzerResults) {
        this.projectAnalyzerResults = projectAnalyzerResults;
    }

    public ProjectMetricsResults calculate(String[] metricsCode) {
        ProjectMetricsResults projectMetricsResults = new ProjectMetricsResults(projectAnalyzerResults.getProjectRoot());
        for (ClassifiedAnalyzerResults classResults : projectAnalyzerResults) {
            ClassMetricsResults classMetricsResults = new ClassMetricsResults(classResults);
            List<SecurityMetric> metrics = new SecurityMetricsFactory().getSecurityMetrics(metricsCode);
            for (SecurityMetric metric : metrics) {
                SecurityMetricValue metricResult = metric.compute(classResults);
                classMetricsResults.add(metricResult);
            }
            projectMetricsResults.add(classMetricsResults);
        }
        return projectMetricsResults;
    }
}
