package org.surface.surface.core.metrics.results;

import com.google.common.collect.Lists;
import org.surface.surface.core.inspection.results.ClassInspectorResults;
import org.surface.surface.core.metrics.results.values.MetricValue;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class ClassMetricsResults implements MetricsResults, Iterable<MetricValue<?>> {
    private final ClassInspectorResults classResults;
    private final List<MetricValue<?>> classValues;

    public ClassMetricsResults(ClassInspectorResults classResults) {
        this.classResults = classResults;
        this.classValues = new ArrayList<>();
    }

    public List<MetricValue<?>> getClassValues() {
        return Collections.unmodifiableList(classValues);
    }

    @Override
    public Iterator<MetricValue<?>> iterator() {
        return getClassValues().iterator();
    }

    public void add(MetricValue<?> classValue) {
        classValues.add(classValue);
    }

    public String getClassName() {
        return classResults.getClassName();
    }

    public String getFullyQualifiedClassName() {
        return classResults.getFullyQualifiedClassName();
    }

    public Path getFilepath() {
        return classResults.getFilepath();
    }

    public ClassInspectorResults getClassResults() {
        return classResults;
    }

    public Map<String, Object> getClassMetrics() {
        Map<String, Object> classMetricsAsMap = new LinkedHashMap<>();
        for (MetricValue<?> classValue : classValues) {
            classMetricsAsMap.put(classValue.getMetricCode(), classValue.getValue());
        }
        return classMetricsAsMap;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Class: " + getFullyQualifiedClassName());
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
