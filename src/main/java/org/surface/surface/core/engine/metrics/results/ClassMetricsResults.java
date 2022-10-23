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
    private static final String KEYWORD_MATCHED_CLASSIFIED_METHODS = "keywordMatchedClassifiedMethods";

    private final String classFullyQualifiedName;
    private final Path filePath;
    private final Map<String, Set<String>> classifiedAttributesMutatorsNames;
    private final Map<String, Set<String>> classifiedAttributesAccessorsNames;
    private final Map<String, Set<String>> classifiedAttributesMethodsNames;
    private final Set<String> keywordMatchedClassifiedMethodsNames;
    private final List<MetricValue<?>> metricValues;

    public ClassMetricsResults(ClassInspectorResults classResults) {
        this.classFullyQualifiedName = classResults.getFullyQualifiedClassName();
        this.filePath = classResults.getFilepath();

        this.classifiedAttributesMutatorsNames = classResults.getClassifiedAttributesMutatorsNames();
        this.classifiedAttributesAccessorsNames = classResults.getClassifiedAttributesAccessorsNames();
        this.classifiedAttributesMethodsNames = classResults.getClassifiedAttributesMethodsNames();
        this.keywordMatchedClassifiedMethodsNames = classResults.getKeywordMatchedClassifiedMethodsNames();;
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

    public Map<String, Object> toMap(Path basePath) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(CLASS_NAME, getClassFullyQualifiedName());
        map.put(FILE_PATH, getFilePath(basePath).toString());
        map.put(CLASS_METRICS, getMetricsAsMap());
        map.put(CLASSIFIED_ATTRIBUTES_METHODS, classifiedAttributesMethodsNames);
        map.put(KEYWORD_MATCHED_CLASSIFIED_METHODS, keywordMatchedClassifiedMethodsNames);
        return map;
    }

    public String toPlain(Path basePath) {
        return "Class: " + getClassFullyQualifiedName() + "\n" +
                "\tFile: " + getFilePath(basePath).toString() + "\n" +
                "\tClass Metrics: " + getMetricsAsPlain() + "\n" +
                "\tClassified Attributes:\n\t\t" + getNamesAsPlain(classifiedAttributesMethodsNames).replace("\n", "\n\t\t") + "\n" +
                "\tKeyword-matched Classified Methods: " + (keywordMatchedClassifiedMethodsNames.size() > 0 ? String.join(", ", keywordMatchedClassifiedMethodsNames).replace("\n", "\n\t") : "None");
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

    private String getNamesAsPlain(Map<String, Set<String>> map) {
        List<String> attributes = new ArrayList<>();
        for (Map.Entry<String, Set<String>> attribute : map.entrySet()) {
            List<String> methods = attribute.getValue()
                    .stream()
                    .map(String::toString)
                    .collect(Collectors.toList());
            attributes.add(attribute.getKey() + ": " + String.join(", ", methods));
        }
        return String.join("\n", attributes);
    }
}
