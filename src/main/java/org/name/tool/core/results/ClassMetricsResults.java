package org.name.tool.core.results;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClassMetricsResults implements Iterable<SecurityMetricValue> {
    private final ClassifiedAnalyzerResults classResults;
    private final List<SecurityMetricValue> results;

    public ClassMetricsResults(ClassifiedAnalyzerResults classResults) {
        this.classResults = classResults;
        this.results = new ArrayList<>();
    }

    @Override
    public Iterator<SecurityMetricValue> iterator() {
        return results.iterator();
    }

    public void add(SecurityMetricValue securityMetricValue) {
        results.add(securityMetricValue);
    }

    public String getClassName() {
        return classResults.getClassName();
    }

    public ClassifiedAnalyzerResults getClassResults() {
        return classResults;
    }

    public List<SecurityMetricValue>getResults() {
        return new ArrayList<>(results);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Class: " + getClassName());
        for (SecurityMetricValue r : this) {
            builder.append("\n");
            builder.append(r.getMetricCode());
            builder.append(" = ");
            builder.append(r.getValue());
        }
        return builder.toString();
    }
}
