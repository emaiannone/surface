package org.surface.surface.core.engine.inspection;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.utils.SymbolSolverCollectionStrategy;
import com.github.javaparser.utils.ProjectRoot;
import com.github.javaparser.utils.SourceRoot;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.inspection.results.InheritanceInspectorResults;
import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ProjectInspector extends Inspector {
    private static final Logger LOGGER = LogManager.getLogger();

    private final ProjectRoot projectRoot;
    private final String filesRegex;
    private final Set<Pattern> classifiedPatterns;
    private final boolean includeTests;

    public ProjectInspector(Path projectAbsolutePath, String filesRegex, Set<Pattern> classifiedPatterns, boolean includeTests) {
        SymbolSolverCollectionStrategy strategy = new SymbolSolverCollectionStrategy();
        strategy.getParserConfiguration().setStoreTokens(false);
        strategy.getParserConfiguration().setAttributeComments(false);
        this.projectRoot = strategy.collect(projectAbsolutePath);
        this.filesRegex = filesRegex;
        this.classifiedPatterns = classifiedPatterns;
        this.includeTests = includeTests;
    }

    public ProjectInspectorResults inspect() throws IOException {
        ProjectInspectorResults projectResults = new ProjectInspectorResults(projectRoot);
        try {
            LOGGER.debug("* Preliminary Inspection started");
            // A SourceRoot is a subdirectory of ProjectRoot containing any root package structure (e.g., src/main/java, src/test/java, target/classes)
            for (SourceRoot sourceRoot : projectRoot.getSourceRoots()) {
                // Check if we ignore test directories
                if (!includeTests) {
                    String dirName = projectRoot.getRoot().relativize(sourceRoot.getRoot()).toString();
                    if (StringUtils.containsIgnoreCase(dirName, "test")) {
                        LOGGER.debug("* Ignoring source root {}", dirName);
                        continue;
                    }
                }
                // Look for Java files in this source root
                List<Path> filesToInspect = JavaFilesExplorer.selectFiles(sourceRoot.getRoot(), filesRegex);
                LOGGER.debug("Java files found: {}", filesToInspect);
                LOGGER.debug("Going to inspect {} files in {}", filesToInspect.size(), sourceRoot.getRoot());
                for (Path filePath : filesToInspect) {
                    // A CompilationUnit represent an AST of a Java file/code snippet
                    ParseResult<CompilationUnit> parseResult = sourceRoot.tryToParse("", filePath.toString());
                    // A CompilationUnit may have multiple TypeDeclaration. Commonly, it has just one (i.e., the top-level class)
                    CompilationUnit compilationUnit = parseResult.getResult().orElse(null);
                    if (!parseResult.isSuccessful() || compilationUnit == null) {
                        LOGGER.debug("Failed to parse file {}", filePath);
                        continue;
                    }
                    // Involve nested classes as well
                    List<TypeDeclaration<?>> allTypes = getAllTypes(compilationUnit.getTypes());
                    for (TypeDeclaration<?> typeDeclaration : allTypes) {
                        if (typeDeclaration.isClassOrInterfaceDeclaration()) {
                            ClassOrInterfaceDeclaration classOrInterfaceDecl = typeDeclaration.asClassOrInterfaceDeclaration();
                            // Consider classes only
                            if (classOrInterfaceDecl.isInterface()) {
                                LOGGER.debug("* Ignoring interface file {}", filePath);
                                continue;
                            }
                            // Check if we ignore files having a "Test"-like method annotation
                            if (!includeTests) {
                                List<AnnotationExpr> allAnnotations = classOrInterfaceDecl.getMethods().stream()
                                        .map(BodyDeclaration::getAnnotations)
                                        .flatMap(List::stream)
                                        .collect(Collectors.toList());
                                if (allAnnotations.stream()
                                        .anyMatch(a -> StringUtils.containsIgnoreCase(a.getNameAsString(), "Test"))) {
                                    LOGGER.debug("* Ignoring test file {}", filePath);
                                    continue;
                                }
                            }
                            ClassInspector classInspector = new ClassInspector(classOrInterfaceDecl, classifiedPatterns, compilationUnit.getStorage().get().getPath(), projectResults);
                            ClassInspectorResults classResults = classInspector.inspect();
                            projectResults.addClassResult(classResults);
                        }
                    }
                }
                InheritanceInspector inheritanceInspector = new InheritanceInspector(projectResults);
                InheritanceInspectorResults inheritanceResults = inheritanceInspector.inspect();
                projectResults.setInheritanceResult(inheritanceResults);
            }
            LOGGER.debug("* Preliminary Inspection ended");
            LOGGER.trace("Preliminary Inspection results:\n\t{}", projectResults.toString().replaceAll("\n", "\n\t"));
            return projectResults;
        } finally {
            // Release! Sadly, the library does not manage well its internal cache, so we have to do this manual clear
            JavaParserFacade.clearInstances();
        }
    }

    private List<TypeDeclaration<?>> getAllTypes(List<TypeDeclaration<?>> types) {
        return types.stream()
                .map(td -> td.findAll(TypeDeclaration.class))
                .flatMap(Collection::stream)
                .map(td -> (TypeDeclaration<?>) td)
                .collect(Collectors.toList());
    }
}
