package org.name.tool.core.metrics.cma;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithPrivateModifier;
import org.name.tool.core.metrics.cm.CM;
import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.SecurityMetricValue;

import java.util.Set;

public class CMAImpl extends CMA {
    private final CM cm;

    public CMAImpl(CM cm) {
        this.cm = cm;
    }

    @Override
    public SecurityMetricValue compute(ClassifiedAnalyzerResults classResults) {
        //TODO Implement
        Set<MethodDeclaration> classifiedMethods = classResults.getClassifiedMethods();
        int nonPrivate = (int) classifiedMethods.stream().filter(ca -> !ca.isPrivate()).count();
        double cmValue = cm.compute(classResults).getValue();
        double value = cmValue != 0.0 ? nonPrivate / cmValue : 0.0;
        return new SecurityMetricValue(getName(), getCode(), value);
    }
}
