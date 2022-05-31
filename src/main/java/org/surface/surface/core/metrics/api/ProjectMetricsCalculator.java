package org.surface.surface.core.metrics.api;

import org.surface.surface.core.inspection.results.ClassInspectorResults;
import org.surface.surface.core.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.metrics.results.ClassMetricsResults;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;

import java.util.List;

public class ProjectMetricsCalculator {
    private final ProjectInspectorResults projectInspectorResults;

    public ProjectMetricsCalculator(ProjectInspectorResults projectInspectorResults) {
        this.projectInspectorResults = projectInspectorResults;
    }

    public ProjectMetricsResults calculate(List<Metric<?, ?>> metrics) {
        ProjectMetricsResults projectMetricsResults = new ProjectMetricsResults(projectInspectorResults.getProjectRoot());
        // Class-level metrics
        for (ClassInspectorResults classResults : projectInspectorResults) {
            ClassMetricsResults classMetricsResults = new ClassMetricsResults(classResults);
            // Class-level metrics
            for (Metric<?, ?> metric : metrics) {
                // TODO Bad solution: change it whenever possible
                if (metric instanceof ClassMetric<?>) {
                    classMetricsResults.add(((ClassMetric<?>) metric).compute(classResults));
                }
            }
            projectMetricsResults.add(classMetricsResults);
        }
        // Project-level metrics
        for (Metric<?, ?> metric : metrics) {
            // TODO Bad solution: change it whenever possible
            if (metric instanceof ProjectMetric<?>) {
                projectMetricsResults.add(((ProjectMetric<?>) metric).compute(projectInspectorResults));
            }
        }
        return projectMetricsResults;
    }
}
