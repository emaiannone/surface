package org.name.tool.results;

import com.github.javaparser.utils.ProjectRoot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ProjectMetricsResults implements Iterable<ClassMetricsResults> {
    private final ProjectRoot projectRoot;
    private final Set<ClassMetricsResults> classMetricsResults;
    private final List<MetricResult<?>> projectMetrics;

    public ProjectMetricsResults(ProjectRoot projectRoot) {
        this.projectRoot = projectRoot;
        this.classMetricsResults = new HashSet<>();
        this.projectMetrics = new ArrayList<>();
    }

    public List<MetricResult<?>> getProjectMetrics() {
        return Collections.unmodifiableList(projectMetrics);
    }

    public Set<ClassMetricsResults> getClassMetricsResults() {
        return Collections.unmodifiableSet(classMetricsResults);
    }

    @Override
    public Iterator<ClassMetricsResults> iterator() {
        return getClassMetricsResults().iterator();
    }

    public void add(ClassMetricsResults classResults) {
        this.classMetricsResults.add(classResults);
    }

    public void add(MetricResult<?> projectMetric) {
        projectMetrics.add(projectMetric);
    }

    public List<String> getProjectMetricsCodes() {
        return projectMetrics.stream()
                .map(MetricResult::getMetricCode)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<?> getProjectMetricsValues() {
        return projectMetrics.stream()
                .map(MetricResult::getValue)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getClassMetricsCodes() {
        return classMetricsResults.stream()
                .flatMap(mr -> mr.getClassMetrics().stream())
                .map(MetricResult::getMetricCode)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Project: " + projectRoot.getRoot().getFileName());
        for (MetricResult<?> projectMetric : projectMetrics) {
            builder.append("\n");
            builder.append(projectMetric.getMetricCode());
            builder.append(" = ");
            builder.append(projectMetric.getValue());
        }
        for (ClassMetricsResults entries : this) {
            if (entries.getClassMetrics().size() > 0) {
                builder.append("\n\n");
                builder.append(entries);
            }
        }
        return builder.toString();
    }
}
