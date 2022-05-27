package org.surface.surface.core.metrics.results;

import com.google.common.collect.Lists;
import org.surface.surface.core.inspection.results.ClassInspectorResults;
import org.surface.surface.core.metrics.results.values.MetricValue;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class ClassMetricsResults implements MetricsResults, Iterable<MetricValue<?>> {
    private final String classFullyQualifiedName;
    private final Path filePath;
    private final Map<String, Set<String>> classifiedAttributesAndMethodsNames;
    private final List<MetricValue<?>> metricValues;

    public ClassMetricsResults(ClassInspectorResults classResults) {
        this.classFullyQualifiedName = classResults.getFullyQualifiedClassName();
        this.filePath = classResults.getFilepath();
        this.classifiedAttributesAndMethodsNames = classResults.getClassifiedAttributesAndMethodsNames();
        this.metricValues = new ArrayList<>();
    }

    public void add(MetricValue<?> classValue) {
        metricValues.add(classValue);
    }

    public String getClassFullyQualifiedName() {
        return classFullyQualifiedName;
    }

    public Path getFilePath(Path basePath) {
        if (basePath != null) {
            return basePath.relativize(filePath);
        }
        return filePath;
    }

    public Map<String, Set<String>> getClassifiedAttributesAndMethodsNames() {
        return classifiedAttributesAndMethodsNames;
    }

    public List<MetricValue<?>> getMetricValues() {
        return Collections.unmodifiableList(metricValues);
    }

    @Override
    public Iterator<MetricValue<?>> iterator() {
        return getMetricValues().iterator();
    }

    public Map<String, Object> getMetrics() {
        Map<String, Object> metricsAsMap = new LinkedHashMap<>();
        for (MetricValue<?> classValue : metricValues) {
            metricsAsMap.put(classValue.getMetricCode(), classValue.getValue());
        }
        return metricsAsMap;
    }

    public Map<String, Object> toMap(Path basePath) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("className", getClassFullyQualifiedName());
        map.put("filePath", getFilePath(basePath).toString());
        map.put("metrics", getMetrics());
        return map;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Class: " + classFullyQualifiedName);
        builder.append(" (");
        List<String> metricStrings = Lists.newArrayList(iterator())
                .stream()
                .map(m -> m.getMetricCode() + "=" + m.getValue())
                .collect(Collectors.toList());
        builder.append(String.join(", ", metricStrings));
        builder.append(")");
        return builder.toString();
    }
}
