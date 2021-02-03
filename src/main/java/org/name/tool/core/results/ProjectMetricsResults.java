package org.name.tool.core.results;

import com.github.javaparser.utils.ProjectRoot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ProjectMetricsResults implements Iterable<ClassMetricsResults> {
    private final ProjectRoot projectRoot;
    private final Set<ClassMetricsResults> classMetricsResultsSet;
    private final List<MetricResult<?>> projectMetrics;

    public ProjectMetricsResults(ProjectRoot projectRoot) {
        this.projectRoot = projectRoot;
        this.classMetricsResultsSet = new HashSet<>();
        this.projectMetrics = new ArrayList<>();
    }

    public Set<ClassMetricsResults> getClassMetricsResultsSet() {
        return Collections.unmodifiableSet(classMetricsResultsSet);
    }

    @Override
    public Iterator<ClassMetricsResults> iterator() {
        return getClassMetricsResultsSet().iterator();
    }

    public void add(ClassMetricsResults classResults) {
        this.classMetricsResultsSet.add(classResults);
    }

    public void add(MetricResult<?> projectMetric) {
        projectMetrics.add(projectMetric);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Project: " + projectRoot.getRoot().getFileName());
        for (MetricResult<?> projectMetric : projectMetrics) {
            builder.append("\n");
            builder.append(projectMetric.getMetricCode());
            builder.append(" = ");
            builder.append(projectMetric.getValue());
        }
        for (ClassMetricsResults entries : this) {
            if (entries.getClassMetrics().size() > 0) {
                builder.append("\n\n");
                builder.append(entries);
            }
        }
        return builder.toString();
    }
}
