package org.name.tool.core.metrics.ccva;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import org.name.tool.core.metrics.ca.CA;
import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.SecurityMetricResult;

import java.util.Set;

public class CCVAImpl extends CCVA {
    private final CA ca;

    public CCVAImpl(CA ca) {
        this.ca = ca;
    }

    @Override
    public SecurityMetricResult<Double> compute(ClassifiedAnalyzerResults classResults) {
        int nonPrivateStatic = 0;
        Set<VariableDeclarator> classifiedAttributes = classResults.getClassifiedAttributes();
        for (VariableDeclarator classifiedAttribute : classifiedAttributes) {
            FieldDeclaration fieldDecl = classifiedAttribute.resolve().asField().toAst().orElse(null);
            if (fieldDecl != null) {
                if (!fieldDecl.isPrivate() && fieldDecl.isStatic()) {
                    nonPrivateStatic++;
                }
            }
        }
        int caValue = ca.compute(classResults).getValue();
        double value = caValue != 0.0 ? (double) nonPrivateStatic / caValue : 0.0;
        return new SecurityMetricResult<>(getName(), getCode(), value);
    }
}
