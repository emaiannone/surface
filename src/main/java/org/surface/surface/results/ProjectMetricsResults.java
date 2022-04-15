package org.surface.surface.results;

import com.github.javaparser.utils.ProjectRoot;
import org.surface.surface.results.values.MetricValue;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class ProjectMetricsResults implements MetricsResults, Iterable<ClassMetricsResults> {
    private final ProjectRoot projectRoot;
    private final Set<ClassMetricsResults> classMetricsResults;
    private final List<MetricValue<?>> projectValues;

    public ProjectMetricsResults(ProjectRoot projectRoot) {
        this.projectRoot = projectRoot;
        this.classMetricsResults = new HashSet<>();
        this.projectValues = new ArrayList<>();
    }

    public Path getProjectRoot() {
        return projectRoot.getRoot();
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

    public Map<String, List<MetricValue<?>>> classMetricsGroupedByCode() {
        Map<String, List<MetricValue<?>>> map = new LinkedHashMap<>();
        for (String classMetricsCode : getClassMetricsCodes()) {
            List<MetricValue<?>> collect = classMetricsResults.stream()
                    .flatMap(mr -> mr.getClassValues().stream())
                    .filter(mr -> mr.getMetricCode().equals(classMetricsCode))
                    .collect(Collectors.toList());
            map.put(classMetricsCode, collect);
        }
        return map;
    }

    public Map<String, Object> getProjectMetrics() {
        Map<String, Object> projectMetricsAsMap = new LinkedHashMap<>();
        for (MetricValue<?> projectValue : projectValues) {
            projectMetricsAsMap.put(projectValue.getMetricCode(), projectValue.getValue());
        }
        return projectMetricsAsMap;
    }

    private List<String> getClassMetricsCodes() {
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
