package org.name.tool.core.results;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.utils.ProjectRoot;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

public class ProjectAnalyzerResults implements AnalyzerResults, Iterable<ClassifiedAnalyzerResults> {
    private final ProjectRoot projectRoot;
    private final Set<ClassifiedAnalyzerResults> results;

    public ProjectAnalyzerResults(ProjectRoot projectRoot) {
        this.projectRoot = projectRoot;
        this.results = new HashSet<>();
    }

    @Override
    public Iterator<ClassifiedAnalyzerResults> iterator() {
        return results.iterator();
    }

    public void add(ClassifiedAnalyzerResults classResults) {
        results.add(classResults);
    }

    public ProjectRoot getProjectRoot() {
        return projectRoot;
    }

    public ClassifiedAnalyzerResults getClassResults(String classQualifiedName) {
        for (ClassifiedAnalyzerResults res : results) {
            if (res.getFullyQualifiedName().equals(classQualifiedName)) {
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Project: " + projectRoot.getRoot().getFileName());
        for (ClassifiedAnalyzerResults entries : this) {
            if (entries.getResults().size() > 0) {
                builder.append("\n");
                builder.append(entries);
            }
        }
        return builder.toString();
    }
}
