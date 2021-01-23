package org.name.tool.core.analysis.results;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

public class ProjectAnalyzerResults implements Iterable<ClassifiedAnalyzerResults> {

    private final Set<ClassifiedAnalyzerResults> results;

    public ProjectAnalyzerResults() {
        this.results = new HashSet<>();
    }

    @Override
    public Iterator<ClassifiedAnalyzerResults> iterator() {
        return results.iterator();
    }

    public void add(ClassifiedAnalyzerResults classResults) {
        results.add(classResults);
    }

    public ClassifiedAnalyzerResults getClassResults(ClassOrInterfaceDeclaration classOrInterfaceDeclaration) {
        for (ClassifiedAnalyzerResults res : results) {
            if (res.getClassOrInterfaceDeclaration().equals(classOrInterfaceDeclaration)) {
                return res;
            }
        }
        return null;
    }

    public Set<MethodDeclaration> getClassifiedMethods() {
        return results
                .stream()
                .map(ClassifiedAnalyzerResults::getClassifiedMethods)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    public Set<VariableDeclarator> getClassifiedAttributes() {
        return results
                .stream()
                .map(ClassifiedAnalyzerResults::getClassifiedAttributes)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }
}
