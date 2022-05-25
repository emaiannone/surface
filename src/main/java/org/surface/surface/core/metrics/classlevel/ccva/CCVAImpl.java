package org.surface.surface.core.metrics.classlevel.ccva;

import com.github.javaparser.ast.body.FieldDeclaration;
import org.surface.surface.core.inspection.results.ClassInspectorResults;
import org.surface.surface.core.metrics.classlevel.ca.CA;
import org.surface.surface.core.metrics.results.values.DoubleMetricValue;

import java.util.Set;

public class CCVAImpl extends CCVA {
    private final CA ca;

    public CCVAImpl(CA ca) {
        this.ca = ca;
    }

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        int nonPrivateStatic = 0;
        Set<FieldDeclaration> correspondingFieldDeclarations = classResults.getCorrespondingFieldDeclarations();
        for (FieldDeclaration correspondingFieldDecl : correspondingFieldDeclarations) {
            if (!correspondingFieldDecl.isPrivate() && correspondingFieldDecl.isStatic()) {
                nonPrivateStatic++;
            }
        }
        int caValue = ca.compute(classResults).getValue();
        double value = caValue != 0.0 ? (double) nonPrivateStatic / caValue : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
