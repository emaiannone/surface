package org.surface.surface.core.engine.inspection;

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
import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;

import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class ClassInspector extends Inspector {
    private final ClassOrInterfaceDeclaration classDeclaration;
    private final Path filepath;
    private final List<Pattern> patterns;

    public ClassInspector(ClassOrInterfaceDeclaration classDeclaration, Path filepath) {
        this.classDeclaration = classDeclaration;
        this.filepath = filepath;
        this.patterns = ClassifiedPatterns.getInstance().getPatterns();
    }

    /**
     * Inspect the class given to the constructor in search of classified attributes and their related classified methods.
     *
     * @return the object representing the results of the inspection of the given class.
     * The results are always not null.
     * If the inspection does not find any classified attribute, the results map will have no content at all.
     * if the inspection does not find any classified method, the results map will only contain the classified attributes (key).
     */
    public ClassInspectorResults inspect() {
        ClassInspectorResults results = new ClassInspectorResults(classDeclaration, filepath);
        Set<VariableDeclarator> classifiedAttributes = getClassifiedAttributes(new LinkedHashSet<>(classDeclaration.getFields()));
        if (classifiedAttributes.size() > 0) {
            Map<VariableDeclarator, Set<MethodDeclaration>> classifiedMethodsMap = getUsageClassifiedMethods(classifiedAttributes);
            for (Map.Entry<VariableDeclarator, Set<MethodDeclaration>> variableDeclaratorSetEntry : classifiedMethodsMap.entrySet()) {
                results.put(variableDeclaratorSetEntry.getKey(), variableDeclaratorSetEntry.getValue());
            }
            // Other classified methods (i.e., name match)
            Set<MethodDeclaration> classifiedMethods = getOtherClassifiedMethods(new LinkedHashSet<>(classDeclaration.getMethods()));
            classifiedMethods.forEach(results::addOtherClassifiedMethod);
        }
        results.setUsingReflection(isUsingReflection());
        return results;
    }

    private Set<VariableDeclarator> getClassifiedAttributes(Set<FieldDeclaration> fieldDeclarations) {
        return fieldDeclarations.stream()
                .flatMap(f -> f.getVariables().stream())
                .filter(this::isClassified)
                .collect(Collectors.toSet());
    }

    /**
     * Inspect the class given to the constructor in search of the classified methods that uses the given classified attributes.
     *
     * @param classifiedAttributes the set of classified attribute for which search for methods that uses (read/write) them.
     * @return a {@link Map} containing for each classified attribute {@link VariableDeclarator} a set of its matched classified methods {@link MethodDeclaration}.
     * Attributes without any matched methods, will result with an empty set of methods.
     */
    private Map<VariableDeclarator, Set<MethodDeclaration>> getUsageClassifiedMethods(Set<VariableDeclarator> classifiedAttributes) {
        // All classified attributes start with an empty set of methods
        Map<VariableDeclarator, Set<MethodDeclaration>> resultMap = new LinkedHashMap<>();
        for (VariableDeclarator variableDeclarator : classifiedAttributes) {
            resultMap.put(variableDeclarator, new LinkedHashSet<>());
        }

        for (MethodDeclaration method : classDeclaration.getMethods()) {
            Set<VariableDeclarator> unmatchedClassifiedAttrSet = new LinkedHashSet<>(classifiedAttributes);
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

    private Set<MethodDeclaration> getOtherClassifiedMethods(Set<MethodDeclaration> methodDeclarations) {
        // Check whether the method name or any or the parameters matches one of the patterns
        return methodDeclarations.stream()
                .filter(m -> isClassified(m) || m.getParameters().stream().anyMatch(this::isClassified))
                .collect(Collectors.toSet());
    }

    private boolean isClassified(NodeWithSimpleName<?> node) {
        return patterns.stream()
                .anyMatch(p -> p.matcher(node.getNameAsString()).matches());
    }

    private boolean isUsingReflection() {
        boolean usingReflection = false;
        CompilationUnit compilationUnit = classDeclaration.findCompilationUnit().orElse(null);
        if (compilationUnit != null) {
            usingReflection = compilationUnit.getImports()
                    .stream()
                    .anyMatch(imp -> imp.getNameAsString().contains("java.lang.reflect"));
        }
        return usingReflection;
    }
}
