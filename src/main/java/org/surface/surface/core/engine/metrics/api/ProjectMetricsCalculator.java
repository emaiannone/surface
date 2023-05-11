package org.surface.surface.core.engine.metrics.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.engine.inspection.ProjectInspector;
import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.ClassMetricsResults;
import org.surface.surface.core.engine.metrics.results.ProjectMetricsResults;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class ProjectMetricsCalculator {
    private static final Logger LOGGER = LogManager.getLogger();

    private final ProjectInspector projectInspector;

    public ProjectMetricsCalculator(Path projectAbsolutePath, String filesRegex, Set<Pattern> classifiedPatterns, boolean includeTests) {
        this.projectInspector = new ProjectInspector(projectAbsolutePath, filesRegex, classifiedPatterns, includeTests);
    }

    public ProjectMetricsResults calculate(MetricsManager metricsManager) throws IOException {
        ProjectInspectorResults projectInspectorResults = projectInspector.inspect();
        ProjectMetricsResults projectMetricsResults = new ProjectMetricsResults(projectInspectorResults.getProjectRoot());
        // Class-level metrics
        for (ClassInspectorResults classResults : projectInspectorResults.getClassResults()) {
            if (!classResults.isCritical()) {
                continue;
            }
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
