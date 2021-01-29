package org.name.tool.core.results;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.resolution.types.ResolvedReferenceType;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassifiedAnalyzerResults implements AnalyzerResults, Iterable<Map.Entry<VariableDeclarator, Set<MethodDeclaration>>> {
    private final ClassOrInterfaceDeclaration classOrInterfaceDeclaration;
    private final Map<VariableDeclarator, Set<MethodDeclaration>> results;
    private boolean usingReflection;

    public ClassifiedAnalyzerResults(ClassOrInterfaceDeclaration classOrInterfaceDeclaration) {
        this.classOrInterfaceDeclaration = classOrInterfaceDeclaration;
        this.results = new HashMap<>();
    }

    @Override
    public Iterator<Map.Entry<VariableDeclarator, Set<MethodDeclaration>>> iterator() {
        return results.entrySet().iterator();
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
        return classOrInterfaceDeclaration.resolve().getQualifiedName();
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

    public Map<VariableDeclarator, Set<MethodDeclaration>> getResults() {
        return new HashMap<>(results);
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

    public Set<MethodDeclaration> getClassifiedMethods(VariableDeclarator variableDeclarator) {
        return results.get(variableDeclarator);
    }

    public List<ResolvedReferenceType> getSuperclasses() {
        return classOrInterfaceDeclaration.resolve().getAncestors();
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
