package org.surface.surface.core.engine.metrics.results;

import com.google.common.collect.Lists;
import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.MetricValue;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class ClassMetricsResults implements MetricsResults, Iterable<MetricValue<?>> {
    private static final String CLASS_NAME = "className";
    private static final String FILE_PATH = "filePath";
    private static final String CLASS_METRICS = "classMetrics";
    private static final String CLASSIFIED_ATTRIBUTES_METHODS = "classifiedAttributesMethods";
    private static final String OTHER_CLASSIFIED_METHODS = "otherClassifiedMethods";

    private final String classFullyQualifiedName;
    private final Path filePath;
    private final Map<String, Set<String>> classifiedAttributesMethodsNames;
    private final Set<String> otherClassifiedMethodsNames;
    private final List<MetricValue<?>> metricValues;

    public ClassMetricsResults(ClassInspectorResults classResults) {
        this.classFullyQualifiedName = classResults.getFullyQualifiedClassName();
        this.filePath = classResults.getFilepath();
        this.classifiedAttributesMethodsNames = classResults.getClassifiedAttributesAndMethodsNames();
        Set<String> otherClassifiedMethodsNames = classResults.getOtherClassifiedMethodsNames();
        otherClassifiedMethodsNames.removeAll(classResults.getUsageClassifiedMethodsNames());
        this.otherClassifiedMethodsNames = otherClassifiedMethodsNames;
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

    private String getClassifiedAttributesMethodsAsPlain() {
        List<String> attributes = new ArrayList<>();
        for (Map.Entry<String, Set<String>> attribute : classifiedAttributesMethodsNames.entrySet()) {
            List<String> methods = attribute.getValue()
                    .stream()
                    .map(String::toString)
                    .collect(Collectors.toList());
            attributes.add(attribute.getKey() + ": " + String.join(", ", methods));
        }
        return String.join("\n", attributes);
    }

    public Map<String, Object> toMap(Path basePath) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(CLASS_NAME, getClassFullyQualifiedName());
        map.put(FILE_PATH, getFilePath(basePath).toString());
        map.put(CLASS_METRICS, getMetricsAsMap());
        map.put(CLASSIFIED_ATTRIBUTES_METHODS, classifiedAttributesMethodsNames);
        map.put(OTHER_CLASSIFIED_METHODS, otherClassifiedMethodsNames);
        return map;
    }

    public String toPlain(Path basePath) {
        return "Class: " + getClassFullyQualifiedName() + "\n" +
                "\tFile: " + getFilePath(basePath).toString() + "\n" +
                "\tClass Metrics: " + getMetricsAsPlain() + "\n" +
                "\tClassified Attributes:\n\t\t" + getClassifiedAttributesMethodsAsPlain().replace("\n", "\n\t\t") + "\n" +
                "\tOther Classified Methods:" + (otherClassifiedMethodsNames.size() > 0 ? "\n\t" + String.join(",", otherClassifiedMethodsNames).replace("\n", "\n\t") : " None");
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
