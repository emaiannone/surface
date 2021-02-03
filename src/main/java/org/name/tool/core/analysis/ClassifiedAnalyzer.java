package org.name.tool.core.analysis;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.resolution.Resolvable;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import org.name.tool.results.ClassifiedAnalyzerResults;

import java.util.ArrayList;
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
            List<NodeWithSimpleName<?>> usageNodes = new ArrayList<>();
            usageNodes.addAll(method.findAll(NameExpr.class));
            usageNodes.addAll(method.findAll(FieldAccessExpr.class));
            for (int i = 0; i < usageNodes.size() && unmatchedClassifiedAttrSet.size() > 0; i++) {
                NodeWithSimpleName<?> usageNode = usageNodes.get(i);
                VariableDeclarator matchedClassifiedAttr = unmatchedClassifiedAttrSet.stream()
                        .filter(ca -> ca.getNameAsString().equals(usageNode.getNameAsString()))
                        .findFirst().orElse(null);
                if (matchedClassifiedAttr != null) {
                    try {
                        // FIXME This is working, but it seems a bad solution... I would like a list of object that are both NodeWithSimpleName and Resolvable<ResolvedValueDeclaration>
                        if (usageNode instanceof Resolvable<?>) {
                            if (((Resolvable<ResolvedValueDeclaration>) usageNode).resolve().isField()) {
                                Set<MethodDeclaration> classifiedMethods = resultMap.get(matchedClassifiedAttr);
                                classifiedMethods.add(method);
                                resultMap.put(matchedClassifiedAttr, classifiedMethods);
                                unmatchedClassifiedAttrSet.remove(matchedClassifiedAttr);
                            }
                        }
                    } catch (RuntimeException | StackOverflowError ignored) {
                        //TODO Improve with logging ERROR. In any case, any raised exception should ignore this usageNode
                        // resolve() raises a number of issues: UnsupportedOperationException, UnsolvedSymbolException, a pure RuntimeException, StackOverflowError
                    }
                }
            }
        }
        return resultMap;
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
