package org.name.tool.core.metrics.api;

import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.ProjectAnalyzerResults;
import org.name.tool.core.metrics.SecurityMetricsFactory;
import org.name.tool.core.results.ClassMetricsResults;
import org.name.tool.core.results.ProjectMetricsResults;
import org.name.tool.core.results.SecurityMetricValue;

import java.util.List;

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
