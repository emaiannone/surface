package org.name.tool.core.metrics.civa;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import org.name.tool.core.analysis.results.ClassifiedAnalyzerResults;
import org.name.tool.core.metrics.api.SecurityMetricValue;
import org.name.tool.core.metrics.ca.ClassifiedAttributes;

import java.util.Set;

public class ClassifiedInstanceVariablesAccessibilityImpl extends ClassifiedInstanceVariablesAccessibility {
    private final ClassifiedAttributes ca;

    public ClassifiedInstanceVariablesAccessibilityImpl(ClassifiedAttributes ca) {
        this.ca = ca;
    }

    @Override
    public SecurityMetricValue compute(ClassifiedAnalyzerResults classResults) {
        int nonPrivateNonStatic = 0;
        Set<VariableDeclarator> classifiedAttributes = classResults.getClassifiedAttributes();
        for (VariableDeclarator classifiedAttribute : classifiedAttributes) {
            FieldDeclaration fieldDecl = classifiedAttribute.resolve().asField().toAst().orElse(null);
            if (fieldDecl != null) {
                if (!fieldDecl.isPrivate() && !fieldDecl.isStatic()) {
                    nonPrivateNonStatic++;
                }
            }
        }
        double caValue = ca.compute(classResults).getValue();
        double value = caValue != 0.0 ? nonPrivateNonStatic / caValue : 0.0;
        return new SecurityMetricValue(this, value);
    }
}