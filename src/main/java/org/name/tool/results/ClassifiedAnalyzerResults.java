package org.name.tool.results;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;

import java.util.*;
import java.util.stream.Collectors;

public class ClassifiedAnalyzerResults implements AnalyzerResults, Iterable<Map.Entry<VariableDeclarator, Set<MethodDeclaration>>> {
    private final ClassOrInterfaceDeclaration classOrInterfaceDeclaration;
    private final Map<VariableDeclarator, Set<MethodDeclaration>> results;
    private boolean usingReflection;

    private Set<FieldDeclaration> correspondingFieldDeclarations;
    private List<ResolvedReferenceType> superClasses;

    public ClassifiedAnalyzerResults(ClassOrInterfaceDeclaration classOrInterfaceDeclaration) {
        this.classOrInterfaceDeclaration = classOrInterfaceDeclaration;
        this.results = new HashMap<>();
        this.correspondingFieldDeclarations = null;
        this.superClasses = null;
    }

    public void put(VariableDeclarator variableDeclarator, Set<MethodDeclaration> methodDeclarations) {
        results.put(variableDeclarator, methodDeclarations);
    }

    public void setUsingReflection(boolean usingReflection) {
        this.usingReflection = usingReflection;
    }

    public Map<VariableDeclarator, Set<MethodDeclaration>> getResults() {
        return Collections.unmodifiableMap(results);
    }

    @Override
    public Iterator<Map.Entry<VariableDeclarator, Set<MethodDeclaration>>> iterator() {
        return getResults().entrySet().iterator();
    }

    public String getClassName() {
        return classOrInterfaceDeclaration.getNameAsString();
    }

    public String getFullyQualifiedClassName() {
        return classOrInterfaceDeclaration.getFullyQualifiedName().orElse(getClassName());
    }

    public Set<MethodDeclaration> getAllMethods() {
        return Collections.unmodifiableSet(new HashSet<>(classOrInterfaceDeclaration.getMethods()));
    }

    public Set<MethodDeclaration> getClassifiedMethods() {
        return Collections.unmodifiableSet(
                results.values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toSet()));
    }

    public Set<MethodDeclaration> getClassifiedMethods(VariableDeclarator variableDeclarator) {
        return Collections.unmodifiableSet(results.get(variableDeclarator));
    }

    public Set<VariableDeclarator> getClassifiedAttributes() {
        return Collections.unmodifiableSet(results.keySet());
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
        this.correspondingFieldDeclarations = new HashSet<>();
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

    public boolean isUsingReflection() {
        return usingReflection;
    }

    public boolean isCritical() {
        return getClassifiedAttributes().size() + getClassifiedMethods().size() > 0;
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
        StringBuilder builder = new StringBuilder("Class: " + classOrInterfaceDeclaration.getNameAsString());
        for (Map.Entry<VariableDeclarator, Set<MethodDeclaration>> e : this) {
            builder.append("\n");
            builder.append(e.getKey().getNameAsString());
            builder.append(" -> ");
            builder.append(e.getValue().stream().map(m -> m.getSignature().toString()).collect(Collectors.toSet()));
        }
        return builder.toString();
    }
}
