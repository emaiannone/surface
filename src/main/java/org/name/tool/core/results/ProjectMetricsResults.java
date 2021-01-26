package org.name.tool.core.results;

import com.github.javaparser.utils.ProjectRoot;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ProjectMetricsResults implements Iterable<ClassMetricsResults> {
    private final ProjectRoot projectRoot;
    private final Set<ClassMetricsResults> results;

    public ProjectMetricsResults(ProjectRoot projectRoot) {
        this.projectRoot = projectRoot;
        this.results = new HashSet<>();
    }

    @Override
    public Iterator<ClassMetricsResults> iterator() {
        return results.iterator();
    }

    public void add(ClassMetricsResults classResults) {
        results.add(classResults);
    }

    public ProjectRoot getProjectRoot() {
        return projectRoot;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Project: " + projectRoot.getRoot().getFileName());
        for (ClassMetricsResults entries : this) {
            if (entries.getResults().size() > 0) {
                builder.append("\n");
                builder.append(entries);
            }
        }
        return builder.toString();
    }
}
