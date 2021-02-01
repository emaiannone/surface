package org.name.tool.core.analysis;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.resolution.Resolvable;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import org.name.tool.core.results.ClassifiedAnalyzerResults;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ClassifiedAnalyzer extends ClassAnalyzer {
    private final List<Pattern> patterns;

    public ClassifiedAnalyzer(ClassOrInterfaceDeclaration classDeclaration) {
        super(classDeclaration);
        this.patterns = ClassifiedPatterns.getInstance().getPatterns();
    }

    /**
     * Analyze the class given to the constructor in search of classified attributes and their related classified methods.
     *
     * @return the object representing the results of the analysis of the given class.
     * The results is always not null.
     * If the analysis does not find any classified attribute, the results map will have no content at all.
     * if the analysis does not find any classified method, the results map will only contain the classified attributes (key).
     */
    public ClassifiedAnalyzerResults analyze() {
        ClassifiedAnalyzerResults results = new ClassifiedAnalyzerResults(getClassDeclaration());
        Set<VariableDeclarator> classifiedAttributes = getClassifiedAttributes(new HashSet<>(getClassDeclaration().getFields()));
        if (classifiedAttributes.size() > 0) {
            Map<VariableDeclarator, Set<MethodDeclaration>> classifiedMethodsMap = getClassifiedMethods(classifiedAttributes);
            for (Map.Entry<VariableDeclarator, Set<MethodDeclaration>> variableDeclaratorSetEntry : classifiedMethodsMap.entrySet()) {
                results.put(variableDeclaratorSetEntry.getKey(), variableDeclaratorSetEntry.getValue());
            }
        }
        results.setUsingReflection(isUsingReflection());
        return results;
    }

    private Set<VariableDeclarator> getClassifiedAttributes(Set<FieldDeclaration> fieldDeclarations) {
        Set<VariableDeclarator> classifiedAttributes = new HashSet<>();
        Set<VariableDeclarator> attributes = fieldDeclarations.stream()
                .flatMap(f -> f.getVariables().stream())
                .collect(Collectors.toSet());
        for (VariableDeclarator attribute : attributes) {
            if (isClassified(attribute)) {
                classifiedAttributes.add(attribute);
            }
        }
        return classifiedAttributes;
    }

    private boolean isClassified(VariableDeclarator attribute) {
        String attributeName = attribute.getNameAsString();
        return patterns.stream()
                .anyMatch(p -> p.matcher(attributeName).matches());
    }

    /**
     * Analyze the class given to the constructor in search of the classified methods of the given classified attributes.
     *
     * @param classifiedAttributes the set of classified attribute for which search for methods that uses (read/write) them.
     * @return a {@link Map} containing for each classified attribute {@link VariableDeclarator} a set of its matched classified methods {@link MethodDeclaration}.
     * Attributes without any matched methods, will result with an empty set of methods.
     */
    private Map<VariableDeclarator, Set<MethodDeclaration>> getClassifiedMethods(Set<VariableDeclarator> classifiedAttributes) {
        // All classified attributes start with an empty set of methods
        Map<VariableDeclarator, Set<MethodDeclaration>> resultMap = new HashMap<>();
        for (VariableDeclarator variableDeclarator : classifiedAttributes) {
            resultMap.put(variableDeclarator, new HashSet<>());
        }

        for (MethodDeclaration method : getClassDeclaration().getMethods()) {
            Set<VariableDeclarator> unmatchedClassifiedAttrSet = new HashSet<>(classifiedAttributes);
            List<NameExpr> nameExprs = method.findAll(NameExpr.class);
            for (int i = 0; i < nameExprs.size() && unmatchedClassifiedAttrSet.size() > 0; i++) {
                NameExpr nameExpr = nameExprs.get(i);
                VariableDeclarator matchedClassifiedAttr = unmatchedClassifiedAttrSet.stream()
                        .filter(ca -> isAttributeUsed(ca, nameExpr))
                        .findFirst().orElse(null);
                if (matchedClassifiedAttr != null) {
                    try {
                        if (nameExpr.resolve().isField()) {
                            Set<MethodDeclaration> classifiedMethods = resultMap.get(matchedClassifiedAttr);
                            classifiedMethods.add(method);
                            resultMap.put(matchedClassifiedAttr, classifiedMethods);
                            unmatchedClassifiedAttrSet.remove(matchedClassifiedAttr);
                        }
                    } catch (UnsolvedSymbolException | UnsupportedOperationException ignored) {
                    }
                }
            }
            // TODO Find a solution for these duplicated loops
            List<FieldAccessExpr> fieldAccessExprs = method.findAll(FieldAccessExpr.class);
            for (int i = 0; i < fieldAccessExprs.size() && unmatchedClassifiedAttrSet.size() > 0; i++) {
                FieldAccessExpr fieldAccessExpr = fieldAccessExprs.get(i);
                VariableDeclarator matchedClassifiedAttr = unmatchedClassifiedAttrSet.stream()
                        .filter(ca -> isAttributeUsed(ca, fieldAccessExpr))
                        .findFirst().orElse(null);
                if (matchedClassifiedAttr != null) {
                    try {
                        if (fieldAccessExpr.resolve().isField()) {
                            Set<MethodDeclaration> classifiedMethods = resultMap.get(matchedClassifiedAttr);
                            classifiedMethods.add(method);
                            resultMap.put(matchedClassifiedAttr, classifiedMethods);
                            unmatchedClassifiedAttrSet.remove(matchedClassifiedAttr);
                        }
                    } catch (UnsolvedSymbolException | UnsupportedOperationException ignored) {
                    }
                }
            }
        }
        return resultMap;
    }

    @Deprecated
    private Set<MethodDeclaration> getClassifiedMethods(VariableDeclarator classifiedAttribute) {
        Set<MethodDeclaration> classifiedMethods = new HashSet<>();
        for (MethodDeclaration method : getClassDeclaration().getMethods()) {
            BlockStmt bodyBlock = method.getBody().orElse(null);
            if (bodyBlock != null) {
                for (NameExpr nameExpr : bodyBlock.findAll(NameExpr.class)) {
                    if (isAttributeUsed(classifiedAttribute, nameExpr)) {
                        try {
                            if (nameExpr.resolve().isField()) {
                                classifiedMethods.add(method);
                            }
                        } catch (UnsolvedSymbolException | UnsupportedOperationException ignored) {
                        }
                    }
                }
                for (FieldAccessExpr fieldAccessExpr : bodyBlock.findAll(FieldAccessExpr.class)) {
                    if (isAttributeUsed(classifiedAttribute, fieldAccessExpr)) {
                        try {
                            if (fieldAccessExpr.resolve().isField()) {
                                classifiedMethods.add(method);
                            }
                        } catch (UnsolvedSymbolException | UnsupportedOperationException ignored) {
                        }
                    }
                }
            }
        }
        return classifiedMethods;
    }

    @Deprecated
    private boolean isFieldDeclared(Resolvable<ResolvedValueDeclaration> resolvable) {
        try {
            ResolvedValueDeclaration resolved = resolvable.resolve();
            return resolved.isField();
        } catch (UnsolvedSymbolException | UnsupportedOperationException e) {
            return false;
        }
    }

    private boolean isAttributeUsed(VariableDeclarator classifiedAttribute, NodeWithSimpleName<?> nodeWithSimpleNames) {
        return classifiedAttribute.getNameAsString().equals(nodeWithSimpleNames.getNameAsString());
    }

    private boolean isUsingReflection() {
        boolean usingReflection = false;
        CompilationUnit compilationUnit = getClassDeclaration().findCompilationUnit().orElse(null);
        if (compilationUnit != null) {
            usingReflection = compilationUnit.getImports()
                    .stream()
                    .anyMatch(imp -> imp.getNameAsString().contains("java.lang.reflect"));
        }
        return usingReflection;
    }
}
