package org.name.tool.core.analysis;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.HashMap;

public class ClassifiedAnalyzer {
    private final ClassOrInterfaceDeclaration classOrInterfaceDeclaration;
    public ClassifiedAnalyzer(ClassOrInterfaceDeclaration classOrInterfaceDeclaration) {
        this.classOrInterfaceDeclaration = classOrInterfaceDeclaration;
    }

    public HashMap<FieldDeclaration, MethodDeclaration> analyze() {
        HashMap<FieldDeclaration, MethodDeclaration> classifiedMap = new HashMap<>();
        for (FieldDeclaration field : classOrInterfaceDeclaration.getFields()) {
            //TODO Extract CAs with the heuristic
            // For each CA, get the CMs
        }
        return classifiedMap;
    }

}
