package org.surface.surface.core.engine.metrics.classlevel.caiw;

import com.github.javaparser.ast.body.VariableDeclarator;
import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.classlevel.cat.CAT;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CAIWImpl extends CAIW {
    private final CAT CAT;

    public CAIWImpl(CAT CAT) {
        this.CAT = CAT;
    }

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        double actualInteractions = 0;
        for (VariableDeclarator attr : classResults.getClassifiedAttributes()) {
            actualInteractions += classResults.getNumberUsageClassifiedMethods(attr);
        }
        double possibleInteractions = CAT.compute(classResults).getValue() * classResults.getNumberAllUsageClassifiedMethods();
        double value = possibleInteractions != 0.0 ? actualInteractions / possibleInteractions : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
