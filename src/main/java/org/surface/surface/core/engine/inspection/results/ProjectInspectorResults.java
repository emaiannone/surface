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

    public List<MethodDeclaration> getAllClassifiedMethods() {
        // Caveat: different methods with same ASTs are considered identical!
        // TODO: this requires a new class wrapping MethodDeclaration so that the equality is done using the class as well
        List<MethodDeclaration> collect = classResults.stream()
                .map(ClassInspectorResults::getClassifiedMethods)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        return Collections.unmodifiableList(collect);
    }

    public List<MethodDeclaration> getAllNonFinalClassifiedMethods() {
        List<MethodDeclaration> collect = getAllClassifiedMethods().stream()
                .filter(cm -> !cm.isFinal())
                .collect(Collectors.toList());
        return Collections.unmodifiableList(collect);
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
        List<MethodDeclaration> allClassifiedMethods = getAllClassifiedMethods();
        for (ClassInspectorResults classResult : classResults) {
            List<MethodDeclaration> otherClassifiedMethods = new ArrayList<>(allClassifiedMethods);
            otherClassifiedMethods.removeIf(cm ->
                    ((ClassOrInterfaceDeclaration) cm.getParentNode().get()).getFullyQualifiedName().get().equals(classResult.getClassFullyQualifiedName())
                            && classResult.getClassifiedMethods().contains(cm)
            );
            Set<MethodDeclaration> intersection = new LinkedHashSet<>(otherClassifiedMethods);
            intersection.retainAll(classResult.getCalledMethods());
            intersection.forEach(md -> classesAccessingClassifiedAttributes.put(classResult.getClassOrInterfaceDeclaration(), md));
        }
        return classesAccessingClassifiedAttributes;
    }

    public int getNumberClasses() {
        return classResults.size();
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

    public int getNumberAllClassifiedAttributes() {
        return getAllClassifiedAttributes()
                .values()
                .stream()
                .mapToInt(Collection::size)
                .sum();
    }

    public int getNumberAllMethods() {
        return classResults.stream()
                .mapToInt(ClassInspectorResults::getNumberMethods)
                .sum();
    }

    public int getNumberAllClassifiedMutators() {
        return classResults.stream()
                .mapToInt(ClassInspectorResults::getNumberClassifiedMutators)
                .sum();
    }

    public int getNumberAllNonPrivateInstanceClassifiedAttributes() {
        return classResults.stream()
                .mapToInt(ClassInspectorResults::getNumberNonPrivateInstanceClassifiedAttributes)
                .sum();
    }

    public int getNumberAllNonPrivateClassClassifiedAttributes() {
        return classResults.stream()
                .mapToInt(ClassInspectorResults::getNumberNonPrivateClassClassifiedAttributes)
                .sum();
    }

    public int getNumberAllNonPrivateClassifiedMethods() {
        return classResults.stream()
                .mapToInt(ClassInspectorResults::getNumberNonPrivateClassifiedMethods)
                .sum();
    }

    public boolean hasClassImportingReflection() {
        return classResults.stream()
                .anyMatch(ClassInspectorResults::isImportingReflection);
    }

    public int getNumberAllPossibleMutatorAttributeInteractions() {
        return classResults.stream()
                .mapToInt(ClassInspectorResults::getNumberPossibleMutatorAttributeInteractions)
                .sum();
    }

    public int getNumberAllActualMutatorAttributeInteractions() {
        return classResults.stream()
                .mapToInt(ClassInspectorResults::getNumberActualMutatorAttributeInteractions)
                .sum();
    }

    public int getNumberAllPossibleAccessorAttributeInteractions() {
        return classResults.stream()
                .mapToInt(ClassInspectorResults::getNumberPossibleAccessorAttributeInteractions)
                .sum();
    }

    public int getNumberAllActualAccessorAttributeInteractions() {
        return classResults.stream()
                .mapToInt(ClassInspectorResults::getNumberActualAccessorAttributeInteractions)
                .sum();
    }

    public int getNumberAllPossibleAttributeInteractions() {
        return classResults.stream()
                .mapToInt(ClassInspectorResults::getNumberPossibleAttributeInteractions)
                .sum();
    }

    public int getNumberAllActualAttributeInteractions() {
        return classResults.stream()
                .mapToInt(ClassInspectorResults::getNumberActualAttributeInteractions)
                .sum();
    }

    public int getNumberAllUnaccessedAssignedAttributes() {
        return classResults.stream()
                .mapToInt(ClassInspectorResults::getNumberUnaccessedAssignedAttributes)
                .sum();
    }

    public int getNumberAllUncalledClassifiedAccessors() {
        return classResults.stream()
                .mapToInt(ClassInspectorResults::getNumberUncalledClassifiedAccessors)
                .sum();
    }

    public int getNumberPossibleAccesses() {
        return (getNumberClasses() - 1) * getNumberAllClassifiedAttributes();
    }

    public int getNumberPossibleInheritances() {
        return (getNumberClasses() - 1) * getNumberCriticalClasses();
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
