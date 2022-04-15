package org.surface.surface.results;

import org.surface.surface.results.values.MetricValue;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ClassMetricsResults implements MetricsResults, Iterable<MetricValue<?>> {
    private final ClassifiedAnalyzerResults classResults;
    private final List<MetricValue<?>> classValues;

    public ClassMetricsResults(ClassifiedAnalyzerResults classResults) {
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

    public Path getFilepath() {
        return classResults.getFilepath();
    }

    public ClassifiedAnalyzerResults getClassResults() {
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
        StringBuilder builder = new StringBuilder("Class: " + getClassName());
        for (MetricValue<?> r : this) {
            builder.append("\n");
            builder.append(r.getMetricCode());
            builder.append(" = ");
            builder.append(r.getValue());
        }
        return builder.toString();
    }
}
