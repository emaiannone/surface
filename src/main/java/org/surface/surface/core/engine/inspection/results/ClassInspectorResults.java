package org.surface.surface.core.engine.inspection.results;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class ClassInspectorResults implements InspectorResults {
    private final ClassOrInterfaceDeclaration classOrInterfaceDeclaration;
    // TODO Create a ClassifiedAttribute class, having VariableDeclarator and FieldDeclaration
    // TODO I don't like Usage and Other classified methods names... consider an alternative and clearer naming
    private final Path filepath;
    private final Map<VariableDeclarator, Set<MethodDeclaration>> classifiedAttributesAndMethods;
    private final Set<MethodDeclaration> otherClassifiedMethods;
    private boolean usingReflection;

    private Set<FieldDeclaration> correspondingFieldDeclarations;
    private List<ResolvedReferenceType> superClasses;

    public ClassInspectorResults(ClassOrInterfaceDeclaration classOrInterfaceDeclaration, Path filepath) {
        this.classOrInterfaceDeclaration = classOrInterfaceDeclaration;
        this.filepath = filepath;
        this.classifiedAttributesAndMethods = new LinkedHashMap<>();
        this.otherClassifiedMethods = new LinkedHashSet<>();
        this.correspondingFieldDeclarations = null;
        this.superClasses = null;
    }

    public void put(VariableDeclarator variableDeclarator, Set<MethodDeclaration> methodDeclarations) {
        classifiedAttributesAndMethods.put(variableDeclarator, methodDeclarations);
    }

    public void addOtherClassifiedMethod(MethodDeclaration methodDeclaration) {
        otherClassifiedMethods.add(methodDeclaration);
    }

    public void setUsingReflection(boolean usingReflection) {
        this.usingReflection = usingReflection;
    }

    private String getClassName() {
        return classOrInterfaceDeclaration.getNameAsString();
    }

    public String getFullyQualifiedClassName() {
        return classOrInterfaceDeclaration.getFullyQualifiedName().orElse(getClassName());
    }

    public Path getFilepath() {
        return filepath;
    }

    public Set<MethodDeclaration> getAllMethods() {
        return Collections.unmodifiableSet(new LinkedHashSet<>(classOrInterfaceDeclaration.getMethods()));
    }

    private Map<VariableDeclarator, Set<MethodDeclaration>> getClassifiedAttributesAndMethods() {
        return Collections.unmodifiableMap(classifiedAttributesAndMethods);
    }

    public Set<VariableDeclarator> getClassifiedAttributes() {
        return Collections.unmodifiableSet(classifiedAttributesAndMethods.keySet());
    }

    public Set<MethodDeclaration> getAllClassifiedMethods() {
        Set<MethodDeclaration> allClassifiedMethods = new LinkedHashSet<>(getUsageClassifiedMethods());
        allClassifiedMethods.addAll(otherClassifiedMethods);
        return Collections.unmodifiableSet(allClassifiedMethods);
    }

    public Set<MethodDeclaration> getUsageClassifiedMethods(VariableDeclarator variableDeclarator) {
        return Collections.unmodifiableSet(classifiedAttributesAndMethods.get(variableDeclarator));
    }

    public Set<MethodDeclaration> getUsageClassifiedMethods() {
        return Collections.unmodifiableSet(
                classifiedAttributesAndMethods.values()
                        .stream()
                        .flatMap(Collection::stream)
                        .collect(Collectors.toSet()));
    }

    public Set<MethodDeclaration> getOtherClassifiedMethods() {
        return Collections.unmodifiableSet(otherClassifiedMethods);
    }

    public List<ResolvedReferenceType> getSuperclasses() {
        // If already computed, return it
        if (this.superClasses != null) {
            return this.superClasses;
        }
        this.superClasses = new ArrayList<>();
        try {
            List<ResolvedReferenceType> directAncestors = classOrInterfaceDeclaration.resolve().getAncestors(true);
            this.superClasses.addAll(getIndirectAncestors(directAncestors));
        } catch (RuntimeException e) {
            //TODO Improve with logging ERROR. In any case, return an empty list
            // resolve() raises a number of issues: UnsupportedOperationException, UnsolvedSymbolException, a pure RuntimeException, StackOverflowError
        }
        return this.superClasses;
    }

    private List<ResolvedReferenceType> getIndirectAncestors(List<ResolvedReferenceType> ancestors) {
        List<ResolvedReferenceType> allAncestors = new ArrayList<>(ancestors);
        for (ResolvedReferenceType ancestor : ancestors) {
            ResolvedReferenceTypeDeclaration resolvedReferenceTypeDeclaration = ancestor.getTypeDeclaration().orElse(null);
            if (resolvedReferenceTypeDeclaration != null) {
                List<ResolvedReferenceType> directAncestors = resolvedReferenceTypeDeclaration.getAncestors(true);
                allAncestors.addAll(getIndirectAncestors(directAncestors));
            }
        }
        return allAncestors;
    }

    public Set<FieldDeclaration> getCorrespondingFieldDeclarations() {
        // If already computed, return it
        if (this.correspondingFieldDeclarations != null) {
            return this.correspondingFieldDeclarations;
        }
        this.correspondingFieldDeclarations = new LinkedHashSet<>();
        for (VariableDeclarator classifiedAttribute : getClassifiedAttributes()) {
            try {
                FieldDeclaration correspondingFieldDecl = classifiedAttribute.resolve().asField().toAst().orElse(null);
                if (correspondingFieldDecl != null) {
                    this.correspondingFieldDeclarations.add(correspondingFieldDecl);
                }
            } catch (RuntimeException ignore) {
                //TODO Improve with logging ERROR. In any case, skip this classifiedAttribute
                // resolve() raises a number of issues: UnsupportedOperationException, UnsolvedSymbolException, a pure RuntimeException, StackOverflowError
            }
        }
        return this.correspondingFieldDeclarations;
    }

    public int getNumberClassifiedAttributes() {
        return getClassifiedAttributes().size();
    }

    public int getNumberAllClassifiedMethods() {
        return getAllClassifiedMethods().size();
    }

    public boolean isUsingReflection() {
        return usingReflection;
    }

    public boolean isCritical() {
        return getNumberClassifiedAttributes() + getNumberAllClassifiedMethods() > 0;
    }

    public boolean isFinal() {
        return classOrInterfaceDeclaration.isFinal();
    }

    public boolean isSerializable() {
        List<ResolvedReferenceType> ancestors = getSuperclasses();
        for (ResolvedReferenceType ancestor : ancestors) {
            if (ancestor.getQualifiedName().equals("java.io.Serializable")) {
                return true;
            }
        }
        return false;
    }

    public Set<String> getClassifiedAttributesNames() {
        return getClassifiedAttributes()
                .stream()
                .map(VariableDeclarator::getNameAsString)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<String> getAllClassifiedMethodsNames() {
        return getAllClassifiedMethods()
                .stream()
                .map(m -> m.getSignature().toString())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Map<String, Set<String>> getClassifiedAttributesAndMethodsNames() {
        Map<String, Set<String>> res = new LinkedHashMap<>();
        for (Map.Entry<VariableDeclarator, Set<MethodDeclaration>> entry : getClassifiedAttributesAndMethods().entrySet()) {
            String attributeName = entry.getKey().getNameAsString();
            LinkedHashSet<String> methodNames = entry.getValue()
                    .stream()
                    .map(m -> m.getSignature().toString())
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            res.put(attributeName, methodNames);
        }
        return res;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Classified Attributes of " + getFullyQualifiedClassName());
        List<String> classifiedAttributesStrings = new ArrayList<>();
        getClassifiedAttributesAndMethodsNames()
                .forEach((key, value) -> classifiedAttributesStrings.add("\n\t" + key + " -> " + value));
        builder.append(String.join(", ", classifiedAttributesStrings));
        List<String> otherMethodsString = otherClassifiedMethods.stream().map(m -> m.getSignature().toString()).collect(Collectors.toList());
        builder.append("\n\tOther Classified Methods: ").append(otherMethodsString);
        return builder.toString();
    }
}
