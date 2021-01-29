package org.name.tool.core.results;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClassMetricsResults implements Iterable<MetricResult<?>> {
    private final ClassifiedAnalyzerResults classResults;
    private final List<MetricResult<?>> classMetrics;

    public ClassMetricsResults(ClassifiedAnalyzerResults classResults) {
        this.classResults = classResults;
        this.classMetrics = new ArrayList<>();
    }

    @Override
    public Iterator<MetricResult<?>> iterator() {
        return classMetrics.iterator();
    }

    public void add(MetricResult<?> metricResult) {
        classMetrics.add(metricResult);
    }

    public String getClassName() {
        return classResults.getClassName();
    }

    public ClassifiedAnalyzerResults getClassResults() {
        return classResults;
    }

    public List<MetricResult<?>> getClassMetrics() {
        return new ArrayList<>(classMetrics);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Class: " + getClassName());
        for (MetricResult<?> r : this) {
            builder.append("\n");
            builder.append(r.getMetricCode());
            builder.append(" = ");
            builder.append(r.getValue());
        }
        return builder.toString();
    }
}
