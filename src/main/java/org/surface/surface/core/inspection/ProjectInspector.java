package org.surface.surface.core.inspection;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.utils.SymbolSolverCollectionStrategy;
import com.github.javaparser.utils.ProjectRoot;
import com.github.javaparser.utils.SourceRoot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.inspection.results.ClassInspectorResults;
import org.surface.surface.core.inspection.results.ProjectInspectorResults;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ProjectInspector extends Inspector {
    private static final Logger LOGGER = LogManager.getLogger();

    private final ProjectRoot projectRoot;
    private final List<Path> allowedFiles;

    public ProjectInspector(Path projectAbsolutePath, List<Path> allowedFiles) {
        SymbolSolverCollectionStrategy symbolSolverCollectionStrategy = new SymbolSolverCollectionStrategy();
        symbolSolverCollectionStrategy.getParserConfiguration().setStoreTokens(false);
        symbolSolverCollectionStrategy.getParserConfiguration().setAttributeComments(false);
        this.projectRoot = symbolSolverCollectionStrategy.collect(projectAbsolutePath);
        this.allowedFiles = allowedFiles;
    }

    public ProjectInspectorResults inspect() throws IOException {
        ProjectInspectorResults projectInspectorResults = new ProjectInspectorResults(projectRoot);
        try {
            LOGGER.info("* Preliminary Inspection started");
            // A SourceRoot is a subdirectory of ProjectRoot containing a root package structure (e.g., src/main/java, src/test/java, target/classes)
            // QUESTION How are non-traditional root package structure but still with Java files handled?
            for (SourceRoot sourceRoot : projectRoot.getSourceRoots()) {
                LOGGER.debug(sourceRoot);
                // NOTE Parse only allowed files... if it doesn't work, parse ALL and then filter using CompilationUnit.getStorage().get().getPath()
                for (Path allowedFile : allowedFiles) {
                    // A CompilationUnit represent an AST of a Java file/code snippet
                    ParseResult<CompilationUnit> parseResult = sourceRoot.tryToParse("", allowedFile.toString());
                    // A CompilationUnit may have multiple TypeDeclaration. Commonly, it has just one (i.e., the top-level class)
                    CompilationUnit compilationUnit = parseResult.getResult().orElse(null);
                    if (!parseResult.isSuccessful() || compilationUnit == null) {
                        LOGGER.debug("Could not parse {}", allowedFile);
                        continue;
                    }
                    for (TypeDeclaration<?> typeDeclaration : compilationUnit.getTypes()) {
                        if (typeDeclaration.isClassOrInterfaceDeclaration()) {
                            ClassOrInterfaceDeclaration classOrInterfaceDecl = typeDeclaration.asClassOrInterfaceDeclaration();
                            // Consider classes only
                            String name = classOrInterfaceDecl.getFullyQualifiedName().orElse(classOrInterfaceDecl.getNameAsString());
                            if (classOrInterfaceDecl.isInterface()) {
                                LOGGER.debug("Ignoring interface {}", name);
                                continue;
                            }
                            LOGGER.debug("Found class: " + name);
                            ClassInspector classInspector = new ClassInspector(classOrInterfaceDecl, compilationUnit.getStorage().get().getPath());
                            ClassInspectorResults classResults = classInspector.inspect();
                            projectInspectorResults.add(classResults);
                        }
                    }
                }
            }
            LOGGER.info("* Preliminary Inspection ended");
            LOGGER.debug("Preliminary Inspection results:\n\t{}", projectInspectorResults.toString().replaceAll("\n", "\n\t"));
            return projectInspectorResults;
        } finally {
            // Release! Sadly, the library does not manage well its internal cache, so we have to do this manual clear
            JavaParserFacade.clearInstances();
        }
    }
}
