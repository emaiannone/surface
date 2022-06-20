package org.surface.surface.core.engine.metrics.classlevel.ccda;

import com.github.javaparser.ast.body.FieldDeclaration;
import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.classlevel.cat.CAT;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

import java.util.Set;

public class CCDAImpl extends CCDA {
    private final CAT CAT;

    public CCDAImpl(CAT CAT) {
        this.CAT = CAT;
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
        int caValue = CAT.compute(classResults).getValue();
        double value = caValue != 0.0 ? (double) nonPrivateStatic / caValue : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
