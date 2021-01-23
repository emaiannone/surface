package org.name.tool.core.analysis.results;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassifiedAnalyzerResults {

    private final ClassOrInterfaceDeclaration classOrInterfaceDeclaration;
    private final Map<VariableDeclarator, Set<MethodDeclaration>> results;

    public ClassifiedAnalyzerResults(ClassOrInterfaceDeclaration classOrInterfaceDeclaration) {
        this.classOrInterfaceDeclaration = classOrInterfaceDeclaration;
        this.results = new HashMap<>();
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

    public void put(VariableDeclarator variableDeclarator, Set<MethodDeclaration> methodDeclarations) {
        results.put(variableDeclarator, methodDeclarations);
    }

    public Set<MethodDeclaration> getClassifiedMethods(VariableDeclarator variableDeclarator) {
        return results.get(variableDeclarator);
    }
}
