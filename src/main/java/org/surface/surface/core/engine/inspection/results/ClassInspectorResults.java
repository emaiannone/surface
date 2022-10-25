package org.surface.surface.core.engine.inspection.results;

import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClassInspectorResults implements InspectorResults {
    private final ClassOrInterfaceDeclaration classOrInterfaceDeclaration;
    private final Path filepath;
    private final ProjectInspectorResults projectResults;

    private final Map<VariableDeclarator, Set<MethodDeclaration>> attributesMutators;
    private final Map<VariableDeclarator, Set<MethodDeclaration>> attributesAccessors;

    // TODO Create a ClassifiedAttribute class, having VariableDeclarator, the corresponding FieldDeclarations, and the mutators and accessors (to remove the three Maps)
    private Map<VariableDeclarator, FieldDeclaration> attributesFieldsCached;
    private List<ResolvedReferenceType> superClassesCached;
    private boolean importingReflection;

    public ClassInspectorResults(ClassOrInterfaceDeclaration classOrInterfaceDeclaration, Path filepath, ProjectInspectorResults projectResults) {
        this.classOrInterfaceDeclaration = classOrInterfaceDeclaration;
        this.filepath = filepath;
        this.projectResults = projectResults;
        this.attributesMutators = new LinkedHashMap<>();
        this.attributesAccessors = new LinkedHashMap<>();
        this.attributesFieldsCached = null;
        this.superClassesCached = null;
    }

    public ClassOrInterfaceDeclaration getClassOrInterfaceDeclaration() {
        return classOrInterfaceDeclaration;
    }

    public void addAccessors(VariableDeclarator attribute, Set<MethodDeclaration> newAccessors) {
        addMethods(attributesAccessors, attribute, newAccessors);
    }

    public void addMutators(VariableDeclarator attribute, Set<MethodDeclaration> newMutators) {
        addMethods(attributesMutators, attribute, newMutators);
    }

    public String getClassFullyQualifiedName() {
        return classOrInterfaceDeclaration.getFullyQualifiedName().orElse(getClassName());
    }

    public Path getFilepath() {
        return filepath;
    }

    public Set<MethodDeclaration> getMethods() {
        return Collections.unmodifiableSet(new LinkedHashSet<>(classOrInterfaceDeclaration.getMethods()));
    }

    public Set<VariableDeclarator> getClassifiedAttributes() {
        return Collections.unmodifiableSet(getAttributesMethods().keySet());
    }

    public Set<MethodDeclaration> getClassifiedMethods() {
        return Collections.unmodifiableSet(mergeMethods(getAttributesMethods()));
    }

    public Set<MethodDeclaration> getNonPrivateClassifiedMethods() {
        return getClassifiedMethods()
                .stream()
                .filter(m -> !m.isPrivate())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<MethodDeclaration> getInheritableClassifiedMethods() {
        return getClassifiedMethods()
                .stream()
                .filter(m -> m.isPublic() || m.isProtected())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public int getNumberMethods() {
        return getMethods().size();
    }

    public int getNumberClassifiedMethods() {
        return getClassifiedMethods().size();
    }

    public int getNumberClassifiedMutators() {
        return getClassifiedMutators().size();
    }

    public int getNumberClassifiedAccessors() {
        return getClassifiedAccessors().size();
    }

    public int getNumberNonPrivateClassifiedMethods() {
        return getNonPrivateClassifiedMethods().size();
    }

    public int getNumberInheritableClassifiedMethods() {
        return getInheritableClassifiedMethods().size();
    }

    public int getNumberClassifiedMethods(VariableDeclarator variableDeclarator) {
        return getAttributesMethods().get(variableDeclarator).size();
    }

    public int getNumberClassifiedAttributes() {
        return getClassifiedAttributes().size();
    }

    public int getNumberClassifiedMutators(VariableDeclarator variableDeclarator) {
        return attributesMutators.get(variableDeclarator).size();
    }

    public int getNumberClassifiedAccessors(VariableDeclarator variableDeclarator) {
        return attributesAccessors.get(variableDeclarator).size();
    }

    public int getNumberPossibleAttributeInteractions() {
        return getNumberClassifiedAttributes() * getNumberClassifiedMethods();
    }

    public int getNumberPossibleMutatorAttributeInteractions() {
        return getNumberClassifiedAttributes() * getNumberClassifiedMutators();
    }

    public int getNumberPossibleAccessorAttributeInteractions() {
        return getNumberClassifiedAttributes() * getNumberClassifiedAccessors();
    }

    public int getNumberActualAttributeInteractions() {
        return (int) getClassifiedAttributes().stream().mapToDouble(this::getNumberClassifiedMethods).sum();
    }

    public int getNumberActualMutatorAttributeInteractions() {
        return (int) getClassifiedAttributes().stream().mapToDouble(this::getNumberClassifiedMutators).sum();
    }

    public int getNumberActualAccessorAttributeInteractions() {
        return (int) getClassifiedAttributes().stream().mapToDouble(this::getNumberClassifiedAccessors).sum();
    }

    public boolean isMutated(VariableDeclarator variableDeclarator) {
        return attributesMutators.containsKey(variableDeclarator);
    }

    public boolean isAccessed(VariableDeclarator variableDeclarator) {
        return attributesAccessors.containsKey(variableDeclarator);
    }

    // TODO any method calling resolve() (e.g., getSuperclass, getSuperclasses, getAttributeFields, getCalledMethods, getUncalledClassifiedAccessors, etc.)
    //  could be moved into ClassInspector as they are actually inspections, and not just queries.
    //  In doing so, we avoid re-computing them multiple times
    public ResolvedReferenceTypeDeclaration getSuperclass() {
        try {
            return classOrInterfaceDeclaration.resolve()
                    .getAncestors(true)
                    .stream()
                    .map(rrt -> rrt.getTypeDeclaration().orElse(null))
                    .filter(Objects::nonNull)
                    .filter(ResolvedTypeDeclaration::isClass)
                    .findFirst()
                    .orElse(null);
        } catch (RuntimeException | StackOverflowError ignored) {
            //TODO Improve with logging ERROR. In any case, any raised exception should ignore this usageNode
            // resolve() raises a number of issues: UnsupportedOperationException, UnsolvedSymbolException, a pure RuntimeException, StackOverflowError
            return null;
        }
    }

    public List<ResolvedReferenceType> getAllSuperclasses() {
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

    public Map<VariableDeclarator, FieldDeclaration> getAttributeFields() {
        // If already computed, return it
        if (attributesFieldsCached != null) {
            return attributesFieldsCached;
        }
        attributesFieldsCached = new LinkedHashMap<>();
        for (VariableDeclarator attr : getClassifiedAttributes()) {
            try {
                attr.resolve().asField().toAst().ifPresent(fd -> attributesFieldsCached.put(attr, fd));
            } catch (RuntimeException ignore) {
                //TODO Improve with logging ERROR. In any case, skip this classifiedAttribute
                // resolve() raises a number of issues: UnsupportedOperationException, UnsolvedSymbolException, a pure RuntimeException, StackOverflowError
            }
        }
        return attributesFieldsCached;
    }

    public Set<MethodDeclaration> getCalledMethods() {
        Set<MethodDeclaration> methodDeclarations = new LinkedHashSet<>();
        List<MethodCallExpr> calledMethods = classOrInterfaceDeclaration.findAll(MethodCallExpr.class);
        for (MethodCallExpr calledMethod : calledMethods) {
            try {
                calledMethod.resolve().toAst().ifPresent(methodDeclarations::add);
            } catch (RuntimeException ignore) {
                //TODO Improve with logging ERROR. In any case, skip this classifiedAttribute
                // resolve() raises a number of issues: UnsupportedOperationException, UnsolvedSymbolException, a pure RuntimeException, StackOverflowError
            }
        }
        return methodDeclarations;
    }

    public Set<VariableDeclarator> getUnaccessedAssignedAttributes() {
        return getClassifiedAttributes()
                .stream()
                .filter(ca -> isMutated(ca) && !isAccessed(ca))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<MethodDeclaration> getUncalledClassifiedAccessors() {
        if (getNumberClassifiedAccessors() == 0) {
            return new LinkedHashSet<>();
        }
        Map<String, MethodDeclaration> accessorMap = new LinkedHashMap<>();
        for (MethodDeclaration accessor : getClassifiedAccessors()) {
            accessorMap.put(accessor.getSignature().toString(), accessor);
        }
        classLoop:
        for (ClassInspectorResults classResult : projectResults.getClassResults()) {
            List<MethodCallExpr> methodCalls = classResult.classOrInterfaceDeclaration.findAll(MethodCallExpr.class);
            for (MethodCallExpr methodCall : methodCalls) {
                try {
                    ResolvedMethodDeclaration resolvedMethodDecl = methodCall.resolve();
                    String calledClassQualifiedName = StringUtils.substringBeforeLast(resolvedMethodDecl.getQualifiedName(), ".");
                    if (calledClassQualifiedName.equals(classResult.getClassFullyQualifiedName())) {
                        MethodDeclaration resolvedMethodDeclNode = resolvedMethodDecl.toAst().orElse(null);
                        if (resolvedMethodDeclNode != null) {
                            String calledMethodSignature = resolvedMethodDeclNode.getSignature().toString();
                            accessorMap.remove(calledMethodSignature);
                            // Nothing more to do, we can end earlier
                            if (accessorMap.isEmpty()) {
                                break classLoop;
                            }
                        }
                    }
                } catch (RuntimeException | StackOverflowError ignored) {
                    //TODO Improve with logging ERROR. In any case, any raised exception should ignore this usageNode
                    // resolve() raises a number of issues: UnsupportedOperationException, UnsolvedSymbolException, a pure RuntimeException, StackOverflowError
                }
            }
        }
        return new LinkedHashSet<>(accessorMap.values());
    }

    public Map<VariableDeclarator, FieldDeclaration> getNonPrivateInstanceClassifiedAttributes() {
        return getAttributeFields().entrySet()
                .stream()
                .filter(e -> !e.getValue().isPrivate() && !e.getValue().isStatic())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y, LinkedHashMap::new));
    }

    public Map<VariableDeclarator, FieldDeclaration> getNonPrivateClassClassifiedAttributes() {
        return getAttributeFields().entrySet()
                .stream()
                .filter(e -> !e.getValue().isPrivate() && e.getValue().isStatic())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y, LinkedHashMap::new));
    }

    public Map<VariableDeclarator, FieldDeclaration> getInheritableClassifiedAttributes() {
        return getAttributeFields().entrySet()
                .stream()
                .filter(e -> e.getValue().isPublic() || e.getValue().isProtected())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y, LinkedHashMap::new));
    }

    public int getNumberUnaccessedAssignedAttributes() {
        return getUnaccessedAssignedAttributes().size();
    }

    public int getNumberUncalledClassifiedAccessors() {
        return getUncalledClassifiedAccessors().size();
    }

    public int getNumberNonPrivateInstanceClassifiedAttributes() {
        return getNonPrivateInstanceClassifiedAttributes().size();
    }

    public int getNumberNonPrivateClassClassifiedAttributes() {
        return getNonPrivateClassClassifiedAttributes().size();
    }

    public int getNumberInheritableClassifiedAttributes() {
        return getInheritableClassifiedAttributes().size();
    }

    public boolean isImportingReflection() {
        return importingReflection;
    }

    public void setImportingReflection(boolean importingReflection) {
        this.importingReflection = importingReflection;
    }

    public boolean isCritical() {
        return getNumberClassifiedAttributes() > 0;
    }

    public boolean isFinal() {
        return classOrInterfaceDeclaration.isFinal();
    }

    public boolean isSerializable() {
        List<ResolvedReferenceType> ancestors = getAllSuperclasses();
        for (ResolvedReferenceType ancestor : ancestors) {
            if (ancestor.getQualifiedName().equals("java.io.Serializable")) {
                return true;
            }
        }
        return false;
    }

    public boolean isNested() {
        return classOrInterfaceDeclaration.isNestedType();
    }

    public boolean hasNestedClass() {
        return getNestedTypes().size() > 0;
    }

    @Override
    public String toString() {
        List<String> classifiedAttributesStrings = new ArrayList<>();
        getClassifiedAttributesMethodsNames()
                .forEach((key, value) -> classifiedAttributesStrings.add("\n\t" + key + " -> " + value));
        return "Classified Attributes of " + getClassFullyQualifiedName() + String.join(", ", classifiedAttributesStrings);
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

    private List<TypeDeclaration<?>> getNestedTypes() {
        return classOrInterfaceDeclaration.asTypeDeclaration()
                .findAll(TypeDeclaration.class)
                .stream()
                .map(td -> (TypeDeclaration<?>) td)
                .filter(TypeDeclaration::isNestedType)
                .collect(Collectors.toList());
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

    private Set<MethodDeclaration> mergeMethods(Map<VariableDeclarator, Set<MethodDeclaration>> map) {
        return map.values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Set<MethodDeclaration> getClassifiedMutators() {
        return Collections.unmodifiableSet(mergeMethods(attributesMutators));
    }

    private Set<MethodDeclaration> getClassifiedAccessors() {
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
