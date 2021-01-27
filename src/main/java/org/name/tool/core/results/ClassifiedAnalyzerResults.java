package org.name.tool.core.results;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassifiedAnalyzerResults implements Iterable<Map.Entry<VariableDeclarator, Set<MethodDeclaration>>> {
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

    public ClassOrInterfaceDeclaration getClassOrInterfaceDeclaration() {
        return classOrInterfaceDeclaration;
    }

    public Set<MethodDeclaration> getClassifiedMethods() {
        return results.values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
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

    public Set<MethodDeclaration> getClassifiedMethods(VariableDeclarator variableDeclarator) {
        return results.get(variableDeclarator);
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
