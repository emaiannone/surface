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
    private final List<MetricValue<?>> projectValues;

    public ProjectMetricsResults(ProjectRoot projectRoot) {
        this.projectRoot = projectRoot;
        this.classMetricsResults = new HashSet<>();
        this.projectValues = new ArrayList<>();
    }

    public List<MetricValue<?>> getProjectValues() {
        return Collections.unmodifiableList(projectValues);
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

    public void add(MetricValue<?> projectValue) {
        projectValues.add(projectValue);
    }

    public List<String> getProjectMetricsCodes() {
        return projectValues.stream()
                .map(MetricValue::getMetricCode)
                .collect(Collectors.toList());
    }

    public List<?> getProjectMetricsValues() {
        return projectValues.stream()
                .map(MetricValue::getValue)
                .collect(Collectors.toList());
    }

    public List<String> getClassMetricsCodes() {
        return classMetricsResults.stream()
                .flatMap(mr -> mr.getClassValues().stream())
                .map(MetricValue::getMetricCode)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Project: " + projectRoot.getRoot().getFileName());
        for (MetricValue<?> projectMetric : projectValues) {
            builder.append("\n");
            builder.append(projectMetric.getMetricCode());
            builder.append(" = ");
            builder.append(projectMetric.getValue());
        }
        for (ClassMetricsResults entries : this) {
            if (entries.getClassValues().size() > 0) {
                builder.append("\n\n");
                builder.append(entries);
            }
        }
        return builder.toString();
    }
}
