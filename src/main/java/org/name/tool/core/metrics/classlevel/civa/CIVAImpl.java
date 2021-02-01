package org.name.tool.core.metrics.classlevel.civa;

import com.github.javaparser.ast.body.FieldDeclaration;
import org.name.tool.core.metrics.classlevel.ca.CA;
import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.MetricResult;

import java.util.Set;

public class CIVAImpl extends CIVA {
    private final CA ca;

    public CIVAImpl(CA ca) {
        this.ca = ca;
    }

    @Override
    public MetricResult<Double> compute(ClassifiedAnalyzerResults classResults) {
        int nonPrivateNonStatic = 0;
        Set<FieldDeclaration> correspondingFieldDeclarations = classResults.getCorrespondingFieldDeclarations();
        for (FieldDeclaration correspondingFieldDecl : correspondingFieldDeclarations) {
            if (!correspondingFieldDecl.isPrivate() && !correspondingFieldDecl.isStatic()) {
                nonPrivateNonStatic++;
            }
        }
        int caValue = ca.compute(classResults).getValue();
        double value = caValue != 0.0 ? (double) nonPrivateNonStatic / caValue : 0.0;
        return new MetricResult<>(getName(), getCode(), value);
    }
}
