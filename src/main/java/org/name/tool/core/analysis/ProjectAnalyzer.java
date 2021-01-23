package org.name.tool.core.analysis;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.symbolsolver.utils.SymbolSolverCollectionStrategy;
import com.github.javaparser.utils.ProjectRoot;
import com.github.javaparser.utils.SourceRoot;
import org.name.tool.core.analysis.results.ClassifiedAnalyzerResults;
import org.name.tool.core.analysis.results.ProjectAnalyzerResults;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class ProjectAnalyzer {
    private final ProjectRoot projectRoot;

    // TODO: Would be good if the list of invalid paths is taken from a file
    public static final List<Path> invalidPaths = Arrays.asList(
            Paths.get("target/classes").toAbsolutePath()
    );

    public ProjectAnalyzer(Path projectAbsolutePath) {
        projectRoot = new SymbolSolverCollectionStrategy().collect(projectAbsolutePath);
    }

    public ProjectAnalyzerResults analyze() {
        // A SourceRoot is a subdirectory of ProjectRoot containing a root package structure (e.g., src/main/java, src/test/java, target/classes)
        ProjectAnalyzerResults projectResults = new ProjectAnalyzerResults(projectRoot);
        for (SourceRoot sourceRoot : projectRoot.getSourceRoots()) {
            if (invalidPaths.contains(sourceRoot.getRoot())) {
                continue;
            }
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
                                    ClassifiedAnalyzerResults classResults = classifiedAnalyzer.analyze();
                                    projectResults.add(classResults);
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("* " + projectResults);
        return projectResults;
    }
}
