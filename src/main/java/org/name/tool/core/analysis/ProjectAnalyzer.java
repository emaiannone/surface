package org.name.tool.core.analysis;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.symbolsolver.utils.SymbolSolverCollectionStrategy;
import com.github.javaparser.utils.ProjectRoot;
import com.github.javaparser.utils.SourceRoot;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

public class ProjectAnalyzer {
    private final ProjectRoot projectRoot;

    public ProjectAnalyzer(Path projectAbsolutePath) {
        projectRoot = new SymbolSolverCollectionStrategy().collect(projectAbsolutePath);
    }

    public HashMap<ClassOrInterfaceDeclaration, HashMap<FieldDeclaration, MethodDeclaration>> analyze() {
        // A SourceRoot is a subdirectory of ProjectRoot containing a root package structure (e.g., src/main/java, src/test/java)
        HashMap<ClassOrInterfaceDeclaration, HashMap<FieldDeclaration, MethodDeclaration>> projectMap = new HashMap<>();
        for (SourceRoot sourceRoot : projectRoot.getSourceRoots()) {
            try {
                List<ParseResult<CompilationUnit>> parseResults = sourceRoot.tryToParse();
                for (ParseResult<CompilationUnit> parseResult : parseResults) {
                    // A CompilationUnit represent an AST of a Java file/code snippet
                    CompilationUnit compilationUnit = parseResult.getResult().orElse(null);
                    if (compilationUnit != null) {
                        // A CompilationUnit may have multiple TypeDeclaration. Commonly, it has just one (i.e., the top-level class)
                        for (TypeDeclaration<?> typeDeclaration : compilationUnit.getTypes()) {
                            // Consider classes only
                            if (typeDeclaration.isClassOrInterfaceDeclaration()) {
                                ClassOrInterfaceDeclaration classOrInterfaceDeclaration = typeDeclaration.asClassOrInterfaceDeclaration();
                                if (!classOrInterfaceDeclaration.isInterface()) {
                                    ClassifiedAnalyzer classifiedAnalyzer = new ClassifiedAnalyzer(classOrInterfaceDeclaration);
                                    HashMap<FieldDeclaration, MethodDeclaration> classMap = classifiedAnalyzer.analyze();
                                    projectMap.put(classOrInterfaceDeclaration, classMap);
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return projectMap;
    }
}
