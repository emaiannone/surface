package org.surface.surface.core.metrics.api;

import org.surface.surface.core.inspection.results.ClassInspectorResults;
import org.surface.surface.core.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.metrics.ClassMetricsFactory;
import org.surface.surface.core.metrics.ProjectMetricsFactory;
import org.surface.surface.core.metrics.results.ClassMetricsResults;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;

import java.util.List;

public class ProjectMetricsCalculator {
    private final ProjectInspectorResults projectInspectorResults;

    public ProjectMetricsCalculator(ProjectInspectorResults projectInspectorResults) {
        this.projectInspectorResults = projectInspectorResults;
    }

    public ProjectMetricsResults calculate(List<String> metricCodes) {
        ProjectMetricsResults projectMetricsResults = new ProjectMetricsResults(projectInspectorResults.getProjectRoot());
        // Class-level metrics
        for (ClassInspectorResults classResults : projectInspectorResults) {
            ClassMetricsResults classMetricsResults = new ClassMetricsResults(classResults);
            List<ClassMetric<?>> classMetrics = ClassMetricsFactory.getMetrics(metricCodes);
            for (ClassMetric<?> classMetric : classMetrics) {
                classMetricsResults.add(classMetric.compute(classResults));
            }
            projectMetricsResults.add(classMetricsResults);
        }
        // Project-level metrics
        List<ProjectMetric<?>> projectMetrics = ProjectMetricsFactory.getMetrics(metricCodes);
        for (ProjectMetric<?> projectMetric : projectMetrics) {
            projectMetricsResults.add(projectMetric.compute(projectInspectorResults));
        }
        return projectMetricsResults;
    }
}
