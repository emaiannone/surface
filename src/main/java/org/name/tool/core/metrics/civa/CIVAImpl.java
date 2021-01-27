package org.name.tool.core.metrics.civa;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.SecurityMetricResult;
import org.name.tool.core.metrics.ca.CA;

import java.util.Set;

public class CIVAImpl extends CIVA {
    private final CA ca;

    public CIVAImpl(CA ca) {
        this.ca = ca;
    }

    @Override
    public SecurityMetricResult<Double> compute(ClassifiedAnalyzerResults classResults) {
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
        int caValue = ca.compute(classResults).getValue();
        double value = caValue != 0.0 ? (double) nonPrivateNonStatic / caValue : 0.0;
        return new SecurityMetricResult<>(getName(), getCode(), value);
    }
}
