package org.surface.surface.core.engine.inspection;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.resolution.Resolvable;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class ClassInspector extends Inspector {
    private final ClassOrInterfaceDeclaration classDeclaration;
    // TODO filepath and projectResults and just forwarded to ClassInspectorResults: does this make sense? Could ProjectInspector pass them directly?
    private final Path filepath;
    private final ProjectInspectorResults projectResults;

    private final List<Pattern> patterns;

    // TODO Move their logic into dedicate classes
    private static final Map<Class<? extends Node>, String> MUTATOR_NODES;
    private static final Map<Class<? extends Node>, String> ACCESSOR_NODES;

    static {
        MUTATOR_NODES = new LinkedHashMap<>();
        MUTATOR_NODES.put(AssignExpr.class, "getTarget");
        MUTATOR_NODES.put(UnaryExpr.class, "getExpression");
        ACCESSOR_NODES = new LinkedHashMap<>();
        ACCESSOR_NODES.put(AssignExpr.class, "getValue");
        ACCESSOR_NODES.put(MethodCallExpr.class, "getArguments");
        ACCESSOR_NODES.put(AssertStmt.class, "getCheck");
        ACCESSOR_NODES.put(IfStmt.class, "getCondition");
        ACCESSOR_NODES.put(SwitchStmt.class, "getSelector");
        ACCESSOR_NODES.put(DoStmt.class, "getCondition");
        ACCESSOR_NODES.put(WhileStmt.class, "getCondition");
        ACCESSOR_NODES.put(ForStmt.class, "getCompare");
        ACCESSOR_NODES.put(ReturnStmt.class, "getExpression");
    }

    public ClassInspector(ClassOrInterfaceDeclaration classDeclaration, Path filepath, ProjectInspectorResults projectResults) {
        this.classDeclaration = classDeclaration;
        this.filepath = filepath;
        this.projectResults = projectResults;
        this.patterns = ClassifiedPatterns.getInstance().getPatterns();
    }

    public ClassInspectorResults inspect() {
        ClassInspectorResults inspectionResults = new ClassInspectorResults(classDeclaration, filepath, projectResults);
        Set<VariableDeclarator> classifiedAttributes = getClassifiedAttributes(new LinkedHashSet<>(classDeclaration.getFields()));
        if (classifiedAttributes.size() > 0) {
            Map<VariableDeclarator, Set<MethodDeclaration>> attributesMutators = getUsageMethods(classifiedAttributes, MUTATOR_NODES);
            Map<VariableDeclarator, Set<MethodDeclaration>> attributesAccessors = getUsageMethods(classifiedAttributes, ACCESSOR_NODES);
            attributesMutators.forEach(inspectionResults::addMutators);
            attributesAccessors.forEach(inspectionResults::addAccessors);
            // Keyword-matched classified methods
            Set<MethodDeclaration> keywordMatchedClassifiedMethods = getKeywordMatchedClassifiedMethods(new LinkedHashSet<>(classDeclaration.getMethods()));
            inspectionResults.addKeywordMatchedClassifiedMethods(keywordMatchedClassifiedMethods);
        }
        inspectionResults.setUsingReflection(isUsingReflection());
        return inspectionResults;
    }

    private Set<VariableDeclarator> getClassifiedAttributes(Set<FieldDeclaration> fieldDeclarations) {
        return fieldDeclarations.stream()
                .flatMap(f -> f.getVariables().stream())
                .filter(this::isClassified)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Map<VariableDeclarator, Set<MethodDeclaration>> getUsageMethods(Set<VariableDeclarator> attributes, Map<Class<? extends Node>, String> nodeTypesToTraverse) {
        // Initialization: all attributes start with an empty set of methods
        Map<VariableDeclarator, Set<MethodDeclaration>> usageMethods = new LinkedHashMap<>();
        for (VariableDeclarator classifiedAttr : attributes) {
            usageMethods.put(classifiedAttr, new LinkedHashSet<>());
        }

        for (MethodDeclaration method : classDeclaration.getMethods()) {
            // Usage nodes collection phase
            for (Map.Entry<Class<? extends Node>, String> nodeType : nodeTypesToTraverse.entrySet()) {
                List<? extends Node> foundNodes = method.findAll(nodeType.getKey());
                for (Node foundNode : foundNodes) {
                    try {
                        Object invokeResult = foundNode.getClass().getMethod(nodeType.getValue()).invoke(foundNode);
                        // Can be Optional<Node>, Node, or NodeList<Expression> -> Put them all in a collection and iterate on all
                        Set<Node> nodesToVisit = new LinkedHashSet<>();
                        if (invokeResult instanceof Optional<?>) {
                            nodesToVisit.add(((Optional<Node>) invokeResult).orElse(null));
                        } else if (invokeResult instanceof Node) {
                            nodesToVisit.add((Node) invokeResult);
                        } else if (invokeResult instanceof NodeList<?>) {
                            nodesToVisit.addAll(new ArrayList<>(((NodeList<Expression>) invokeResult)));
                        }
                        for (Node nodeToVisit : nodesToVisit) {
                            Set<NodeWithSimpleName<?>> usageNodes = new LinkedHashSet<>();
                            usageNodes.addAll(nodeToVisit.findAll(NameExpr.class));
                            usageNodes.addAll(nodeToVisit.findAll(FieldAccessExpr.class));
                            // Verification phase: which attributes does this method use?
                            for (NodeWithSimpleName<?> usageNode : usageNodes) {
                                // For each attribute that matched with the name
                                Set<VariableDeclarator> matchedAttributes = attributes.stream()
                                        .filter(attr -> attr.getNameAsString().equals(usageNode.getNameAsString()))
                                        .collect(Collectors.toCollection(LinkedHashSet::new));
                                for (VariableDeclarator matchedAttribute : matchedAttributes) {
                                    try {
                                        // FIXME This works, but it seems a bad solution... I would like a list of object that are both NodeWithSimpleName and Resolvable<ResolvedValueDeclaration>
                                        // Ensure that the name can be resolve into a field, i.e., is it really referring to that attribute?
                                        if (usageNode instanceof Resolvable) {
                                            if (((Resolvable<ResolvedValueDeclaration>) usageNode).resolve().isField()) {
                                                usageMethods.get(matchedAttribute).add(method);
                                            }
                                        }
                                    } catch (RuntimeException | StackOverflowError ignored) {
                                        //TODO Improve with logging ERROR. In any case, any raised exception should ignore this usageNode
                                        // resolve() raises a number of issues: UnsupportedOperationException, UnsolvedSymbolException, a pure RuntimeException, StackOverflowError
                                    }
                                }
                            }
                        }
                    } catch (NoSuchMethodException
                             | InvocationTargetException
                             | IllegalAccessException ignored) {
                        // TODO Improve logging ERROR
                    }
                }
            }
        }
        return usageMethods;
    }

    private Set<MethodDeclaration> getKeywordMatchedClassifiedMethods(Set<MethodDeclaration> methodDeclarations) {
        // Check whether the method name or any or the parameters matches one of the patterns
        return methodDeclarations.stream()
                .filter(m -> isClassified(m) || m.getParameters().stream().anyMatch(this::isClassified))
                .collect(Collectors.toCollection(LinkedHashSet::new));
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
