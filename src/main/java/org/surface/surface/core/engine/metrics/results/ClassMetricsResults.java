package org.surface.surface.core.engine.metrics.results;

import com.google.common.collect.Lists;
import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.MetricValue;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class ClassMetricsResults implements MetricsResults, Iterable<MetricValue<?>> {
    public static final String CLASS_NAME = "className";
    public static final String FILE_PATH = "filePath";
    public static final String CLASS_METRICS = "classMetrics";
    public static final String CLASSIFIED_ATTRIBUTES = "classifiedAttributes";

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

    private String getClassFullyQualifiedName() {
        return classFullyQualifiedName;
    }

    private Path getFilePath(Path basePath) {
        if (basePath != null) {
            return basePath.relativize(filePath);
        }
        return filePath;
    }

    public List<MetricValue<?>> getMetricValues() {
        return Collections.unmodifiableList(metricValues);
    }

    @Override
    public Iterator<MetricValue<?>> iterator() {
        return getMetricValues().iterator();
    }

    private Map<String, Object> getMetricsAsMap() {
        Map<String, Object> metricsAsMap = new LinkedHashMap<>();
        for (MetricValue<?> classValue : metricValues) {
            metricsAsMap.put(classValue.getMetricCode(), classValue.getValue());
        }
        return metricsAsMap;
    }

    private String getMetricsAsPlain() {
        List<String> metricStrings = getMetricsAsMap().entrySet()
                .stream()
                .map(m -> m.getKey() + "=" + m.getValue())
                .collect(Collectors.toList());
        return String.join(", ", metricStrings);
    }

    private String getClassifiedAttributesAsPlain() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Set<String>> attribute : classifiedAttributesAndMethodsNames.entrySet()) {
            List<String> methods = attribute.getValue()
                    .stream()
                    .map(String::toString)
                    .collect(Collectors.toList());
            sb.append(attribute.getKey()).append(": ").append(String.join(", ", methods)).append("\n");
        }
        return sb.toString();
    }

    public Map<String, Object> toMap(Path basePath) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(CLASS_NAME, getClassFullyQualifiedName());
        map.put(FILE_PATH, getFilePath(basePath).toString());
        map.put(CLASS_METRICS, getMetricsAsMap());
        map.put(CLASSIFIED_ATTRIBUTES, classifiedAttributesAndMethodsNames);
        return map;
    }

    public String toPlain(Path basePath) {
        // Convert the content into a human-readable format
        return "Class: " + getClassFullyQualifiedName() + "\n" +
                "File: " + getFilePath(basePath).toString() + "\n" +
                "Class Metrics: " + getMetricsAsPlain() + "\n" +
                "Classified Attributes: " + getClassifiedAttributesAsPlain().replace("\n", "\n  ");
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
