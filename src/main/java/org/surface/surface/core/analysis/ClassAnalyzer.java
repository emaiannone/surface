package org.surface.surface.core.analysis;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import org.surface.surface.results.ClassifiedAnalyzerResults;

public abstract class ClassAnalyzer {
    private final ClassOrInterfaceDeclaration classDeclaration;

    public ClassAnalyzer(ClassOrInterfaceDeclaration classDeclaration) {
        this.classDeclaration = classDeclaration;
    }

    public ClassOrInterfaceDeclaration getClassDeclaration() {
        return classDeclaration;
    }

    public abstract ClassifiedAnalyzerResults analyze();
}
