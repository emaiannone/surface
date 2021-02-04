package org.name.tool.results;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.utils.ProjectRoot;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

public class ProjectAnalyzerResults implements AnalyzerResults, Iterable<ClassifiedAnalyzerResults> {
    private final ProjectRoot projectRoot;
    private final Set<ClassifiedAnalyzerResults> results;

    public ProjectAnalyzerResults(ProjectRoot projectRoot) {
        this.projectRoot = projectRoot;
        this.results = new HashSet<>();
    }

    public void add(ClassifiedAnalyzerResults classResults) {
        results.add(classResults);
    }

    public Set<ClassifiedAnalyzerResults> getResults() {
        return Collections.unmodifiableSet(results);
    }

    @Override
    public Iterator<ClassifiedAnalyzerResults> iterator() {
        return getResults().iterator();
    }

    public ProjectRoot getProjectRoot() {
        return projectRoot;
    }

    public ClassifiedAnalyzerResults getClassResults(String classQualifiedName) {
        for (ClassifiedAnalyzerResults res : results) {
            if (res.getFullyQualifiedClassName().equals(classQualifiedName)) {
                return res;
            }
        }
        return null;
    }

    public Set<MethodDeclaration> getClassifiedMethods() {
        return Collections.unmodifiableSet(
                results.stream()
                .map(ClassifiedAnalyzerResults::getClassifiedMethods)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet()));
    }

    public Set<ClassifiedAnalyzerResults> getCriticalClasses() {
        Set<ClassifiedAnalyzerResults> criticalClasses = new HashSet<>();
        for (ClassifiedAnalyzerResults classResults : results) {
            if (classResults.isCritical()) {
                criticalClasses.add(classResults);
            }
        }
        return Collections.unmodifiableSet(criticalClasses);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Project: " + projectRoot.getRoot().getFileName());
        for (ClassifiedAnalyzerResults entries : results) {
            if (entries.getResults().size() > 0) {
                builder.append("\n");
                builder.append(entries);
            }
        }
        return builder.toString();
    }
}
