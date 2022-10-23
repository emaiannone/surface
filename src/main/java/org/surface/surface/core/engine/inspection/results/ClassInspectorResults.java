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
import java.util.stream.Stream;

public class ClassInspectorResults implements InspectorResults {
    private final ClassOrInterfaceDeclaration classOrInterfaceDeclaration;
    private final Path filepath;
    private final Map<VariableDeclarator, Set<MethodDeclaration>> attributesMutators;
    private final Map<VariableDeclarator, Set<MethodDeclaration>> attributesAccessors;
    private final Set<MethodDeclaration> keywordMatchedClassifiedMethods;
    private boolean usingReflection;

    // TODO Create a ClassifiedAttribute class, having VariableDeclarator and the corresponding FieldDeclarations (to remove correspondingFieldDeclarations)
    private Set<FieldDeclaration> correspondingFieldDeclsCached;
    private List<ResolvedReferenceType> superClassesCached;

    public ClassInspectorResults(ClassOrInterfaceDeclaration classOrInterfaceDeclaration, Path filepath) {
        this.classOrInterfaceDeclaration = classOrInterfaceDeclaration;
        this.filepath = filepath;
        this.attributesMutators = new LinkedHashMap<>();
        this.attributesAccessors = new LinkedHashMap<>();
        this.keywordMatchedClassifiedMethods = new LinkedHashSet<>();
        this.correspondingFieldDeclsCached = null;
        this.superClassesCached = null;
    }

    public void addAccessors(VariableDeclarator attribute, Set<MethodDeclaration> newAccessors) {
        addMethods(attributesAccessors, attribute, newAccessors);
    }

    public void addMutators(VariableDeclarator attribute, Set<MethodDeclaration> newMutators) {
        addMethods(attributesMutators, attribute, newMutators);
    }

    public void addKeywordMatchedClassifiedMethods(Set<MethodDeclaration> methods) {
        keywordMatchedClassifiedMethods.addAll(methods);
    }

    public void addAccessor(VariableDeclarator attribute, MethodDeclaration newAccessor) {
        addMethod(attributesAccessors, attribute, newAccessor);
    }

    public void addMutator(VariableDeclarator attribute, MethodDeclaration newMutator) {
        addMethod(attributesMutators, attribute, newMutator);
    }

    public void addKeywordMatchedClassifiedMethod(MethodDeclaration method) {
        keywordMatchedClassifiedMethods.add(method);
    }

    public void setUsingReflection(boolean usingReflection) {
        this.usingReflection = usingReflection;
    }

    public String getFullyQualifiedClassName() {
        return classOrInterfaceDeclaration.getFullyQualifiedName().orElse(getClassName());
    }

    public Path getFilepath() {
        return filepath;
    }

    public Set<MethodDeclaration> getClassMethods() {
        return Collections.unmodifiableSet(new LinkedHashSet<>(classOrInterfaceDeclaration.getMethods()));
    }

    public Set<VariableDeclarator> getClassifiedAttributes() {
        return Collections.unmodifiableSet(getAttributesMethods().keySet());
    }

    public Set<MethodDeclaration> getClassifiedUsageMethods(VariableDeclarator variableDeclarator) {
        return Collections.unmodifiableSet(getAttributesMethods().get(variableDeclarator));
    }

    public Set<MethodDeclaration> getAllClassifiedMethods() {
        Set<MethodDeclaration> allClassifiedMethods = new LinkedHashSet<>(getAllClassifiedUsageMethods());
        allClassifiedMethods.addAll(keywordMatchedClassifiedMethods);
        return Collections.unmodifiableSet(allClassifiedMethods);
    }

    public int getNumberClassMethods() {
        return classOrInterfaceDeclaration.getMethods().size();
    }

    public int getNumberClassifiedAttributes() {
        return getAttributesMethods().keySet().size();
    }

    public boolean isMutated(VariableDeclarator variableDeclarator) {
        return attributesMutators.containsKey(variableDeclarator);
    }

    public boolean isAccessed(VariableDeclarator variableDeclarator) {
        return attributesAccessors.containsKey(variableDeclarator);
    }

    public int getNumberClassifiedMutators(VariableDeclarator variableDeclarator) {
        return attributesMutators.get(variableDeclarator).size();
    }

    public int getNumberClassifiedAccessors(VariableDeclarator variableDeclarator) {
        return attributesAccessors.get(variableDeclarator).size();
    }

    public int getNumberClassifiedUsageMethods(VariableDeclarator variableDeclarator) {
        return getAttributesMethods().get(variableDeclarator).size();
    }

    public int getNumberAllClassifiedMutators() {
        return getAllClassifiedMutators().size();
    }

    public int getNumberAllClassifiedAccessors() {
        return getAllClassifiedAccessors().size();
    }

    public int getNumberAllClassifiedUsageMethods() {
        return getAllClassifiedUsageMethods().size();
    }

    public int getNumberAllClassifiedMethods() {
        return getAllClassifiedMethods().size();
    }

    public List<ResolvedReferenceType> getSuperclasses() {
        // If already computed, return it
        if (this.superClassesCached != null) {
            return this.superClassesCached;
        }
        this.superClassesCached = new ArrayList<>();
        try {
            List<ResolvedReferenceType> directAncestors = classOrInterfaceDeclaration.resolve().getAncestors(true);
            this.superClassesCached.addAll(getIndirectAncestors(directAncestors));
        } catch (RuntimeException e) {
            //TODO Improve with logging ERROR. In any case, return an empty list
            // resolve() raises a number of issues: UnsupportedOperationException, UnsolvedSymbolException, a pure RuntimeException, StackOverflowError
        }
        return this.superClassesCached;
    }

    public Set<FieldDeclaration> getCorrespondingFieldDeclarations() {
        // If already computed, return it
        if (this.correspondingFieldDeclsCached != null) {
            return this.correspondingFieldDeclsCached;
        }
        this.correspondingFieldDeclsCached = new LinkedHashSet<>();
        for (VariableDeclarator classifiedAttribute : getClassifiedAttributes()) {
            try {
                FieldDeclaration correspondingFieldDecl = classifiedAttribute.resolve().asField().toAst().orElse(null);
                if (correspondingFieldDecl != null) {
                    this.correspondingFieldDeclsCached.add(correspondingFieldDecl);
                }
            } catch (RuntimeException ignore) {
                //TODO Improve with logging ERROR. In any case, skip this classifiedAttribute
                // resolve() raises a number of issues: UnsupportedOperationException, UnsolvedSymbolException, a pure RuntimeException, StackOverflowError
            }
        }
        return this.correspondingFieldDeclsCached;
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

    @Override
    public String toString() {
        List<String> classifiedAttributesStrings = new ArrayList<>();
        getClassifiedAttributesMethodsNames()
                .forEach((key, value) -> classifiedAttributesStrings.add("\n\t" + key + " -> " + value));
        List<String> keywordMethodsString = new ArrayList<>(getKeywordMatchedClassifiedMethodsNames());
        return "Classified Attributes of " + getFullyQualifiedClassName() + String.join(", ", classifiedAttributesStrings) +
                "\n\tKeyword-matched Classified Methods: " + String.join(", ", keywordMethodsString);
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

    public Set<String> getClassifiedUsageMethodsNames() {
        return getAllClassifiedUsageMethods()
                .stream()
                .map(m -> m.getSignature().toString())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<String> getKeywordMatchedClassifiedMethodsNames() {
        return keywordMatchedClassifiedMethods
                .stream()
                .map(m -> m.getSignature().toString())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Map<String, Set<String>> getClassifiedAttributesMutatorsNames() {
        return getNames(attributesMutators);
    }

    public Map<String, Set<String>> getClassifiedAttributesAccessorsNames() {
        return getNames(attributesAccessors);
    }

    public Map<String, Set<String>> getClassifiedAttributesMethodsNames() {
        return getNames(getAttributesMethods());
    }

    private void addMethod(Map<VariableDeclarator, Set<MethodDeclaration>> structure, VariableDeclarator attribute, MethodDeclaration newMethod) {
        Set<MethodDeclaration> methods;
        if (structure.containsKey(attribute)) {
            methods = structure.computeIfAbsent(attribute, k -> new LinkedHashSet<>());
        } else {
            methods = new LinkedHashSet<>();
            structure.put(attribute, methods);
        }
        methods.add(newMethod);
    }

    private void addMethods(Map<VariableDeclarator, Set<MethodDeclaration>> structure, VariableDeclarator attribute, Set<MethodDeclaration> newMethods) {
        Set<MethodDeclaration> methods;
        if (structure.containsKey(attribute)) {
            methods = structure.computeIfAbsent(attribute, k -> new LinkedHashSet<>());
        } else {
            methods = new LinkedHashSet<>();
            structure.put(attribute, methods);
        }
        methods.addAll(newMethods);
    }

    private String getClassName() {
        return classOrInterfaceDeclaration.getNameAsString();
    }

    private Map<String, Set<String>> getNames(Map<VariableDeclarator, Set<MethodDeclaration>> map) {
        Map<String, Set<String>> res = new LinkedHashMap<>();
        map.forEach((k, v) -> res.put(
                        k.getNameAsString(),
                        v.stream().map(m -> m.getSignature().toString()).collect(Collectors.toCollection(LinkedHashSet::new))
                )
        );
        return res;
    }

    private Map<VariableDeclarator, Set<MethodDeclaration>> getAttributesMethods() {
        Map<VariableDeclarator, Set<MethodDeclaration>> attributesMethods = new LinkedHashMap<>(attributesMutators);
        attributesAccessors.forEach((k, v) -> attributesMethods
                .merge(k, v, (a, b) -> Stream.concat(a.stream(), b.stream()).collect(Collectors.toCollection(LinkedHashSet::new)))
        );
        return Collections.unmodifiableMap(attributesMethods);
    }

    private Map<VariableDeclarator, Set<MethodDeclaration>> getAttributesMutators() {
        return Collections.unmodifiableMap(attributesMutators);
    }

    private Map<VariableDeclarator, Set<MethodDeclaration>> getAttributesAccessors() {
        return Collections.unmodifiableMap(attributesAccessors);
    }

    private Set<MethodDeclaration> getKeywordMatchedClassifiedMethods() {
        return Collections.unmodifiableSet(keywordMatchedClassifiedMethods);
    }

    private Set<MethodDeclaration> mergeMethods(Map<VariableDeclarator, Set<MethodDeclaration>> map) {
        return map.values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Set<MethodDeclaration> getAllClassifiedUsageMethods() {
        return Collections.unmodifiableSet(mergeMethods(getAttributesMethods()));
    }

    private Set<MethodDeclaration> getAllClassifiedMutators() {
        return Collections.unmodifiableSet(mergeMethods(attributesMutators));
    }

    private Set<MethodDeclaration> getAllClassifiedAccessors() {
        return Collections.unmodifiableSet(mergeMethods(attributesAccessors));
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
}
