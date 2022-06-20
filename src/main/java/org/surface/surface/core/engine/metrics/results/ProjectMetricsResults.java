package org.surface.surface.core.engine.metrics.results;

import com.github.javaparser.utils.ProjectRoot;
import org.surface.surface.core.engine.metrics.results.values.MetricValue;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class ProjectMetricsResults implements MetricsResults, Iterable<ClassMetricsResults> {
    private static final String PROJECT_NAME = "projectName";
    private static final String PROJECT_DIR = "projectDir";
    private static final String PROJECT_METRICS = "projectMetrics";
    private static final String CLASSES = "classes";
    private final Path projectRoot;
    private final Set<ClassMetricsResults> classMetricsResults;
    private final List<MetricValue<?>> metricValues;

    public ProjectMetricsResults(ProjectRoot projectRoot) {
        this.projectRoot = projectRoot.getRoot();
        this.classMetricsResults = new LinkedHashSet<>();
        this.metricValues = new ArrayList<>();
    }

    public void add(ClassMetricsResults classResults) {
        this.classMetricsResults.add(classResults);
    }

    public void add(MetricValue<?> projectValue) {
        metricValues.add(projectValue);
    }

    public String getProjectName() {
        return projectRoot.getFileName().toString();
    }

    public Path getProjectPath() {
        return projectRoot.toAbsolutePath();
    }

    private Set<ClassMetricsResults> getClassMetricsResults() {
        return Collections.unmodifiableSet(classMetricsResults);
    }

    @Override
    public Iterator<ClassMetricsResults> iterator() {
        return getClassMetricsResults().iterator();
    }

    public List<MetricValue<?>> getMetricValues() {
        return Collections.unmodifiableList(metricValues);
    }

    public Map<String, List<MetricValue<?>>> classMetricsGroupedByCode() {
        Map<String, List<MetricValue<?>>> map = new LinkedHashMap<>();
        for (String classMetricsCode : getClassMetricsCodes()) {
            List<MetricValue<?>> collect = classMetricsResults.stream()
                    .flatMap(mr -> mr.getMetricValues().stream())
                    .filter(mr -> mr.getMetricCode().equals(classMetricsCode))
                    .collect(Collectors.toList());
            map.put(classMetricsCode, collect);
        }
        return map;
    }

    private Map<String, Object> getMetrics() {
        Map<String, Object> metricsAsMap = new LinkedHashMap<>();
        for (MetricValue<?> projectValue : metricValues) {
            metricsAsMap.put(projectValue.getMetricCode(), projectValue.getValue());
        }
        return metricsAsMap;
    }

    private List<String> getClassMetricsCodes() {
        return classMetricsResults.stream()
                .flatMap(mr -> mr.getMetricValues().stream())
                .map(MetricValue::getMetricCode)
                .distinct()
                .collect(Collectors.toList());
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(PROJECT_NAME, getProjectName());
        map.put(PROJECT_DIR, getProjectPath().toString());
        map.put(PROJECT_METRICS, getMetrics());
        List<Map<?, ?>> classes = new ArrayList<>();
        for (ClassMetricsResults classMetricsResult : getClassMetricsResults()) {
            classes.add(classMetricsResult.toMap(getProjectPath()));
        }
        map.put(CLASSES, classes);
        return map;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Project: " + projectRoot.getRoot().toAbsolutePath());
        builder.append(" (");
        List<String> metricStrings = metricValues
                .stream()
                .map(m -> m.getMetricCode() + "=" + m.getValue())
                .collect(Collectors.toList());
        builder.append(String.join(", ", metricStrings));
        builder.append(")");

        for (ClassMetricsResults entries : this) {
            if (entries.getMetricValues().size() > 0) {
                builder.append("\n");
                builder.append(entries);
            }
        }
        return builder.toString();
    }
}
