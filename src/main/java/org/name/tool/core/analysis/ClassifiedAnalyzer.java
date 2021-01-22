package org.name.tool.core.analysis;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import org.name.tool.core.analysis.results.ClassifiedAnalyzerResults;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClassifiedAnalyzer {
    private final ClassOrInterfaceDeclaration classOrInterfaceDeclaration;

    public ClassifiedAnalyzer(ClassOrInterfaceDeclaration classOrInterfaceDeclaration) {
        this.classOrInterfaceDeclaration = classOrInterfaceDeclaration;
    }

    public ClassifiedAnalyzerResults analyze() {
        System.out.println("* Analyzing class: " + classOrInterfaceDeclaration.getName());
        ClassifiedAnalyzerResults results = new ClassifiedAnalyzerResults(classOrInterfaceDeclaration);
        List<VariableDeclarator> classifiedAttributes = getClassifiedAttributes(classOrInterfaceDeclaration.getFields());
        //TODO For each CA, get its CMs

        return results;
    }

    private List<VariableDeclarator> getClassifiedAttributes(List<FieldDeclaration> fieldDeclarations) {
        List<VariableDeclarator> classifiedAttributes = new ArrayList<>();
        List<VariableDeclarator> attributes = fieldDeclarations.stream()
                .flatMap(f -> f.getVariables().stream())
                .collect(Collectors.toList());
        for (VariableDeclarator attribute : attributes) {
            if (isClassified(attribute)) {
                classifiedAttributes.add(attribute);
            }
        }
        return classifiedAttributes;
    }

    private boolean isClassified(VariableDeclarator attribute) {
        //TODO Implement: check heuristic
        System.out.println(attribute);
        return true;
    }

}
