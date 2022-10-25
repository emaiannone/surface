package org.surface.surface.core.engine.inspection.results;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.utils.ProjectRoot;

import java.util.*;
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

    public Set<ClassInspectorResults> getClassResults() {
        return Collections.unmodifiableSet(classResults);
    }

    public InheritanceInspectorResults getInheritanceResult() {
        return inheritanceResult;
    }

    public void setInheritanceResult(InheritanceInspectorResults inheritanceResult) {
        this.inheritanceResult = inheritanceResult;
    }

    public ClassInspectorResults getClassResult(String classQualifiedName) {
        return classResults.stream()
                .filter(ir -> ir.getClassFullyQualifiedName().equals(classQualifiedName))
                .findFirst().orElse(null);
    }

    public ProjectRoot getProjectRoot() {
        return projectRoot;
    }

    public Map<ClassOrInterfaceDeclaration, Set<VariableDeclarator>> getAllClassifiedAttributes() {
        Map<ClassOrInterfaceDeclaration, Set<VariableDeclarator>> allClassifiedAttributes = new LinkedHashMap<>();
        classResults.forEach(cr -> allClassifiedAttributes.put(cr.getClassOrInterfaceDeclaration(), cr.getClassifiedAttributes()));
        return allClassifiedAttributes;
    }

    public Set<MethodDeclaration> getAllClassifiedMethods() {
        Set<MethodDeclaration> collect = classResults.stream()
                .map(ClassInspectorResults::getClassifiedMethods)
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return Collections.unmodifiableSet(collect);
    }

    public Set<MethodDeclaration> getAllNonFinalClassifiedMethods() {
        Set<MethodDeclaration> collect = getAllClassifiedMethods().stream()
                .filter(cm -> !cm.isFinal())
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return Collections.unmodifiableSet(collect);
    }

    public Set<ClassOrInterfaceDeclaration> getCriticalClasses() {
        Set<ClassOrInterfaceDeclaration> criticalClasses = getCriticalClassesInspectorResults()
                .stream()
                .map(ClassInspectorResults::getClassOrInterfaceDeclaration)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return Collections.unmodifiableSet(criticalClasses);
    }

    public Set<ClassOrInterfaceDeclaration> getSerializableCriticalClasses() {
        Set<ClassOrInterfaceDeclaration> criticalClasses = getCriticalClassesInspectorResults()
                .stream()
                .filter(ClassInspectorResults::isSerializable)
                .map(ClassInspectorResults::getClassOrInterfaceDeclaration)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return Collections.unmodifiableSet(criticalClasses);
    }

    public Set<ClassOrInterfaceDeclaration> getCriticalClassesUncalledAccessor() {
        Set<ClassOrInterfaceDeclaration> criticalClasses = getCriticalClassesInspectorResults()
                .stream()
                .filter(cir -> cir.getNumberUncalledClassifiedAccessors() > 0)
                .map(ClassInspectorResults::getClassOrInterfaceDeclaration)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return Collections.unmodifiableSet(criticalClasses);
    }

    public Set<ClassOrInterfaceDeclaration> getNestedCriticalClasses() {
        return getCriticalClassesInspectorResults().stream()
                .filter(ClassInspectorResults::isNested)
                .map(ClassInspectorResults::getClassOrInterfaceDeclaration)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<ClassOrInterfaceDeclaration> getNonFinalClassifiedClasses() {
        return getCriticalClassesInspectorResults().stream()
                .filter(cir -> !cir.isFinal())
                .map(ClassInspectorResults::getClassOrInterfaceDeclaration)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<ClassOrInterfaceDeclaration> getCriticalClassesWithNested() {
        return getCriticalClassesInspectorResults().stream()
                .filter(ClassInspectorResults::hasNestedClass)
                .map(ClassInspectorResults::getClassOrInterfaceDeclaration)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Map<ClassOrInterfaceDeclaration, MethodDeclaration> getClassesAccessingClassifiedAttributes() {
        Map<ClassOrInterfaceDeclaration, MethodDeclaration> classesAccessingClassifiedAttributes = new LinkedHashMap<>();
        Set<MethodDeclaration> allClassifiedMethods = getAllClassifiedMethods();
        for (ClassInspectorResults classResult : classResults) {
            Set<MethodDeclaration> otherClassifiedMethods = new LinkedHashSet<>(allClassifiedMethods);
            otherClassifiedMethods.removeAll(classResult.getClassifiedMethods());
            Set<MethodDeclaration> calledMethods = classResult.getCalledMethods();
            Set<MethodDeclaration> intersection = new LinkedHashSet<>(otherClassifiedMethods);
            intersection.retainAll(calledMethods);
            intersection.forEach(md -> classesAccessingClassifiedAttributes.put(classResult.getClassOrInterfaceDeclaration(), md));
        }
        return classesAccessingClassifiedAttributes;
    }

    public int getNumberClasses() {
        return classResults.size();
    }

    public int getNumberAllClassifiedAttributes() {
        return (int) getAllClassifiedAttributes()
                .values()
                .stream()
                .mapToLong(Collection::size)
                .sum();
    }

    public int getNumberPossibleAccesses() {
        return (getNumberClasses() - 1) * getNumberAllClassifiedAttributes();
    }

    public int getNumberPossibleInheritances() {
        return (getNumberClasses() - 1) * getNumberCriticalClasses();
    }

    public int getNumberAllClassifiedMethods() {
        return getAllClassifiedMethods().size();
    }

    public int getNumberAllNonFinalClassifiedMethods() {
        return getAllNonFinalClassifiedMethods().size();
    }

    public int getNumberSerializableCriticalClasses() {
        return getSerializableCriticalClasses().size();
    }

    public int getNumberCriticalClasses() {
        return getCriticalClasses().size();
    }

    public int getNumberCriticalClassesUncalledAccessor() {
        return getCriticalClassesUncalledAccessor().size();
    }

    public int getNumberNestedCriticalClasses() {
        return getNestedCriticalClasses().size();
    }

    public int getNumberNonFinalClassifiedClasses() {
        return getNonFinalClassifiedClasses().size();
    }

    public int getNumberCriticalClassesWithNested() {
        return getCriticalClassesWithNested().size();
    }

    public int getNumberClassesAccessingClassifiedAttributes() {
        return getClassesAccessingClassifiedAttributes().size();
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

    private Set<ClassInspectorResults> getCriticalClassesInspectorResults() {
        return classResults.stream()
                .filter(ClassInspectorResults::isCritical)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
