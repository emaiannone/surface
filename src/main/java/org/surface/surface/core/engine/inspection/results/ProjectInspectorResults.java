package org.surface.surface.core.engine.inspection.results;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.utils.ProjectRoot;

import java.util.*;
import java.util.stream.Collectors;

public class ProjectInspectorResults implements InspectorResults, Iterable<ClassInspectorResults> {
    private final ProjectRoot projectRoot;
    private final Set<ClassInspectorResults> results;

    public ProjectInspectorResults(ProjectRoot projectRoot) {
        this.projectRoot = projectRoot;
        this.results = new LinkedHashSet<>();
    }

    public void add(ClassInspectorResults classResults) {
        results.add(classResults);
    }

    public Set<ClassInspectorResults> getResults() {
        return Collections.unmodifiableSet(results);
    }

    @Override
    public Iterator<ClassInspectorResults> iterator() {
        return getResults().iterator();
    }

    public ProjectRoot getProjectRoot() {
        return projectRoot;
    }

    public ClassInspectorResults getClassResults(String classQualifiedName) {
        for (ClassInspectorResults res : results) {
            if (res.getFullyQualifiedClassName().equals(classQualifiedName)) {
                return res;
            }
        }
        return null;
    }

    public Set<MethodDeclaration> getClassifiedMethods() {
        return Collections.unmodifiableSet(
                results.stream()
                        .map(ClassInspectorResults::getAllClassifiedMethods)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toSet()));
    }

    public Set<ClassInspectorResults> getCriticalClasses() {
        Set<ClassInspectorResults> criticalClasses = results.stream()
                .filter(ClassInspectorResults::isCritical)
                .collect(Collectors.toCollection(HashSet::new));
        return Collections.unmodifiableSet(criticalClasses);
    }

    public int getNumberCriticalClasses() {
        return getCriticalClasses().size();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Project: " + projectRoot.getRoot().toAbsolutePath());
        for (ClassInspectorResults entries : results) {
            builder.append("\n");
            builder.append(entries);
        }
        return builder.toString();
    }
}
