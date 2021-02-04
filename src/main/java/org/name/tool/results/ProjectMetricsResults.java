package org.name.tool.results;

import com.github.javaparser.utils.ProjectRoot;

import java.util.*;
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

    public Map<String, List<MetricValue<?>>> classMetricsGroupedByCode() {
        Map<String, List<MetricValue<?>>> map = new HashMap<>();
        for (String classMetricsCode : getClassMetricsCodes()) {
            List<MetricValue<?>> collect = classMetricsResults.stream()
                    .flatMap(mr -> mr.getClassValues().stream())
                    .filter(mr -> mr.getMetricCode().equals(classMetricsCode))
                    .collect(Collectors.toList());
            map.put(classMetricsCode, collect);
        }
        return map;
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
