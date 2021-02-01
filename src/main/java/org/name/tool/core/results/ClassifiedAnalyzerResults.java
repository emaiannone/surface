package org.name.tool.core.results;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
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

    public ClassifiedAnalyzerResults(ClassOrInterfaceDeclaration classOrInterfaceDeclaration) {
        this.classOrInterfaceDeclaration = classOrInterfaceDeclaration;
        this.results = new HashMap<>();
    }

    public Map<VariableDeclarator, Set<MethodDeclaration>> getResults() {
        return new HashMap<>(results);
    }

    @Override
    public Iterator<Map.Entry<VariableDeclarator, Set<MethodDeclaration>>> iterator() {
        return getResults().entrySet().iterator();
    }

    public void put(VariableDeclarator variableDeclarator, Set<MethodDeclaration> methodDeclarations) {
        results.put(variableDeclarator, methodDeclarations);
    }

    public void setUsingReflection(boolean usingReflection) {
        this.usingReflection = usingReflection;
    }

    public String getClassName() {
        return classOrInterfaceDeclaration.getNameAsString();
    }

    public String getFullyQualifiedName() {
        return classOrInterfaceDeclaration.getFullyQualifiedName().orElse(getClassName());
    }

    ClassOrInterfaceDeclaration getClassOrInterfaceDeclaration() {
        return classOrInterfaceDeclaration;
    }

    public Set<MethodDeclaration> getClassifiedMethods() {
        return results.values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    public Set<MethodDeclaration> getMethods() {
        return new HashSet<>(classOrInterfaceDeclaration.getMethods());
    }

    public Set<VariableDeclarator> getClassifiedAttributes() {
        return results.keySet();
    }

    public List<ResolvedReferenceType> getSuperclasses() {
        try {
            List<ResolvedReferenceType> directAncestors = classOrInterfaceDeclaration.resolve().getAncestors(true);
            return getIndirectAncestors(directAncestors);
        } catch (RuntimeException e) {
            //TODO Improve with logging ERROR. In any case, return an empty list
            // resolve() raises a number of issues: UnsupportedOperationException, UnsolvedSymbolException, a pure RuntimeException, StackOverflowError
            return new ArrayList<>();
        }
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

    public Set<MethodDeclaration> getClassifiedMethods(VariableDeclarator variableDeclarator) {
        return results.get(variableDeclarator);
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
