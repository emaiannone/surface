package org.surface.surface.core.metrics.classlevel.cida;

import com.github.javaparser.ast.body.FieldDeclaration;
import org.surface.surface.core.inspection.results.ClassInspectorResults;
import org.surface.surface.core.metrics.classlevel.cat.CAT;
import org.surface.surface.core.metrics.results.values.DoubleMetricValue;

import java.util.Set;

public class CIDAImpl extends CIDA {
    private final CAT CAT;

    public CIDAImpl(CAT CAT) {
        this.CAT = CAT;
    }

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        int nonPrivateNonStatic = 0;
        Set<FieldDeclaration> correspondingFieldDeclarations = classResults.getCorrespondingFieldDeclarations();
        for (FieldDeclaration correspondingFieldDecl : correspondingFieldDeclarations) {
            if (!correspondingFieldDecl.isPrivate() && !correspondingFieldDecl.isStatic()) {
                nonPrivateNonStatic++;
            }
        }
        int caValue = CAT.compute(classResults).getValue();
        double value = caValue != 0.0 ? (double) nonPrivateNonStatic / caValue : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
