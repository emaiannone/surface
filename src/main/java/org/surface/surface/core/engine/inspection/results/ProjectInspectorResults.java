package org.surface.surface.core.engine.inspection.results;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.utils.ProjectRoot;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ProjectInspectorResults implements InspectorResults {
    private final ProjectRoot projectRoot;
    private final Set<ClassInspectorResults> classResults;
    private InheritanceInspectorResults inheritanceResult;

    public ProjectInspectorResults(ProjectRoot projectRoot) {
        this.projectRoot = projectRoot;
        this.classResults = new LinkedHashSet<>();
        this.inheritanceResult = null;
    }

    public void addClassResult(ClassInspectorResults classResult) {
        this.classResults.add(classResult);
    }

    public void setInheritanceResult(InheritanceInspectorResults inheritanceResult) {
        this.inheritanceResult = inheritanceResult;
    }

    public Set<ClassInspectorResults> getClassResults() {
        return Collections.unmodifiableSet(classResults);
    }

    public InheritanceInspectorResults getInheritanceResult() {
        return inheritanceResult;
    }

    public ClassInspectorResults getClassResult(String classQualifiedName) {
        return classResults.stream()
                .filter(ir -> ir.getClassFullyQualifiedName().equals(classQualifiedName))
                .findFirst().orElse(null);
    }

    public ProjectRoot getProjectRoot() {
        return projectRoot;
    }

    public Set<MethodDeclaration> getAllClassifiedMethods() {
        Set<MethodDeclaration> collect = classResults.stream()
                .map(ClassInspectorResults::getAllClassifiedMethods)
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return Collections.unmodifiableSet(collect);
    }

    public Set<ClassInspectorResults> getCriticalClasses() {
        Set<ClassInspectorResults> criticalClasses = classResults.stream()
                .filter(ClassInspectorResults::isCritical)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return Collections.unmodifiableSet(criticalClasses);
    }

    public int getNumberClasses() {
        return classResults.size();
    }

    public int getNumberAllClassifiedMethods() {
        return getAllClassifiedMethods().size();
    }

    public int getNumberCriticalClasses() {
        return getCriticalClasses().size();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Project: " + projectRoot.getRoot().toAbsolutePath());
        for (ClassInspectorResults entries : classResults) {
            builder.append("\n");
            builder.append(entries);
        }
        return builder.toString();
    }
}
