package org.name.tool.core.analysis;

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
import org.name.tool.core.analysis.results.ClassifiedAnalyzerResults;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ClassifiedAnalyzer {
    private final ClassOrInterfaceDeclaration classDeclaration;

    // TODO Add more patterns
    public static final Pattern[] patterns = new Pattern[]{
            Pattern.compile("username*", Pattern.CASE_INSENSITIVE),
            Pattern.compile("password*", Pattern.CASE_INSENSITIVE),
            Pattern.compile("passphrase*", Pattern.CASE_INSENSITIVE),
            Pattern.compile("key*", Pattern.CASE_INSENSITIVE),
            Pattern.compile("token*", Pattern.CASE_INSENSITIVE),
            Pattern.compile("secret*", Pattern.CASE_INSENSITIVE)
    };

    public ClassifiedAnalyzer(ClassOrInterfaceDeclaration classDeclaration) {
        this.classDeclaration = classDeclaration;
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
        System.out.println("* Analyzing class: " + classDeclaration.getName());
        ClassifiedAnalyzerResults results = new ClassifiedAnalyzerResults(classDeclaration);
        Set<VariableDeclarator> classifiedAttributes = getClassifiedAttributes(new HashSet<>(classDeclaration.getFields()));
        System.out.println("** Classified Attributes: " + classifiedAttributes);
        if (classifiedAttributes.size() > 0) {
            for (VariableDeclarator attribute : classifiedAttributes) {
                System.out.println("** Searching classified method of: " + attribute);
                Set<MethodDeclaration> classifiedMethods = getClassifiedMethods(attribute);
                results.put(attribute, classifiedMethods);
            }
        }
        //System.out.println("* " + results);
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
        return Arrays.stream(patterns)
                .anyMatch(p -> p.matcher(attributeName).matches());
    }

    /**
     * Analyze the class given to the constructor in search of the classified methods of the given classified attributes.
     *
     * @param classifiedAttribute the classified attribute for which search for methods that uses (read/write) it.
     * @return the set of classified methods of the given classified attribute.
     * If no methods are found, the set will be empty.
     */
    private Set<MethodDeclaration> getClassifiedMethods(VariableDeclarator classifiedAttribute) {
        Set<MethodDeclaration> classifiedMethods = new HashSet<>();
        for (MethodDeclaration method : classDeclaration.getMethods()) {
            BlockStmt bodyBlock = method.getBody().orElse(null);
            if (bodyBlock != null) {
                Set<NodeWithSimpleName<?>> usagesNameExpr = bodyBlock
                        .findAll(NameExpr.class)
                        .stream()
                        .filter(this::isFieldDeclared)
                        .collect(Collectors.toSet());
                Set<NodeWithSimpleName<?>> usagesFieldAccess = bodyBlock
                        .findAll(FieldAccessExpr.class)
                        .stream()
                        .filter(this::isFieldDeclared)
                        .collect(Collectors.toSet());
                boolean classifiedUsedInNameExpr = isAttributeUsed(classifiedAttribute, usagesNameExpr);
                boolean classifiedUsedInFieldAccess = isAttributeUsed(classifiedAttribute, usagesFieldAccess);
                if (classifiedUsedInNameExpr || classifiedUsedInFieldAccess) {
                    System.out.println("** Method " + method.getSignature().toString() + " has no usages.");
                    classifiedMethods.add(method);
                } else {
                    System.out.println("** Method " + method.getSignature().toString() + " has a usage.");
                }
            }
        }
        return classifiedMethods;
    }

    private boolean isFieldDeclared(Resolvable<ResolvedValueDeclaration> resolvable) {
        try {
            return resolvable.resolve().isField();
        } catch (UnsolvedSymbolException e) {
            return false;
        }
    }

    private boolean isAttributeUsed(VariableDeclarator classifiedAttribute, Set<NodeWithSimpleName<?>> nodeWithSimpleNames) {
        return nodeWithSimpleNames.stream()
                .map(NodeWithSimpleName::getNameAsString)
                .anyMatch(name -> name.equals(classifiedAttribute.getNameAsString()));
    }
}
