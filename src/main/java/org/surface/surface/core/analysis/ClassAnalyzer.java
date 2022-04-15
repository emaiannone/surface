package org.surface.surface.core.analysis;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import org.surface.surface.results.ClassifiedAnalyzerResults;

import java.nio.file.Path;

public abstract class ClassAnalyzer {
    private final ClassOrInterfaceDeclaration classDeclaration;
    private final Path filepath;

    public ClassAnalyzer(ClassOrInterfaceDeclaration classDeclaration, Path filepath) {
        this.classDeclaration = classDeclaration;
        this.filepath = filepath;
    }

    public ClassOrInterfaceDeclaration getClassDeclaration() {
        return classDeclaration;
    }

    public Path getFilepath() {
        return filepath;
    }

    public abstract ClassifiedAnalyzerResults analyze();
}
