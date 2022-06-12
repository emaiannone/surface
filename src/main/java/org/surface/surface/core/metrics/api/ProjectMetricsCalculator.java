package org.surface.surface.core.metrics.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.inspection.ProjectInspector;
import org.surface.surface.core.inspection.results.ClassInspectorResults;
import org.surface.surface.core.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.metrics.results.ClassMetricsResults;
import org.surface.surface.core.metrics.results.ProjectMetricsResults;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ProjectMetricsCalculator {
    private static final Logger LOGGER = LogManager.getLogger();

    private final ProjectInspector projectInspector;

    public ProjectMetricsCalculator(Path projectAbsolutePath, String filesRegex, boolean includeTests) {
        this.projectInspector = new ProjectInspector(projectAbsolutePath, filesRegex, includeTests);
    }

    public ProjectMetricsResults calculate(MetricsManager metricsManager) throws IOException {
        ProjectInspectorResults projectInspectorResults = projectInspector.inspect();
        ProjectMetricsResults projectMetricsResults = new ProjectMetricsResults(projectInspectorResults.getProjectRoot());
        // Class-level metrics
        for (ClassInspectorResults classResults : projectInspectorResults) {
            ClassMetricsResults classMetricsResults = new ClassMetricsResults(classResults);
            // Class-level metrics
            List<ClassMetric<?>> classMetrics = metricsManager.prepareClassMetrics();
            for (ClassMetric<?> metric : classMetrics) {
                classMetricsResults.add(metric.compute(classResults));
            }
            projectMetricsResults.add(classMetricsResults);
        }
        // Project-level metrics
        List<ProjectMetric<?>> projectMetrics = metricsManager.prepareProjectMetrics();
        for (ProjectMetric<?> metric : projectMetrics) {
            projectMetricsResults.add(metric.compute(projectInspectorResults));
        }
        return projectMetricsResults;
    }
}
