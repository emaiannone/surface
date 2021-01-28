package org.name.tool.core.results;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClassMetricsResults implements Iterable<SecurityMetricResult<?>> {
    private final ClassifiedAnalyzerResults classResults;
    private final List<SecurityMetricResult<?>> classMetrics;

    public ClassMetricsResults(ClassifiedAnalyzerResults classResults) {
        this.classResults = classResults;
        this.classMetrics = new ArrayList<>();
    }

    @Override
    public Iterator<SecurityMetricResult<?>> iterator() {
        return classMetrics.iterator();
    }

    public void add(SecurityMetricResult<?> securityMetricResult) {
        classMetrics.add(securityMetricResult);
    }

    public String getClassName() {
        return classResults.getClassName();
    }

    public ClassifiedAnalyzerResults getClassResults() {
        return classResults;
    }

    public List<SecurityMetricResult<?>> getClassMetrics() {
        return new ArrayList<>(classMetrics);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Class: " + getClassName());
        for (SecurityMetricResult<?> r : this) {
            builder.append("\n");
            builder.append(r.getMetricCode());
            builder.append(" = ");
            builder.append(r.getValue());
        }
        return builder.toString();
    }
}
