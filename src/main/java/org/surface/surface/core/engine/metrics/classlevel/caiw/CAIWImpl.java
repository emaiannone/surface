package org.surface.surface.core.engine.metrics.classlevel.caiw;

import com.github.javaparser.ast.body.VariableDeclarator;
import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.classlevel.cat.CAT;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CAIWImpl extends CAIW {
    private final CAT cat;

    public CAIWImpl(CAT cat) {
        this.cat = cat;
    }

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        double actualInteractions = 0;
        for (VariableDeclarator attr : classResults.getClassifiedAttributes()) {
            actualInteractions += classResults.getNumberClassifiedMethods(attr);
        }
        double possibleInteractions = cat.compute(classResults).getValue() * classResults.getNumberClassifiedMethods();
        double value = possibleInteractions != 0.0 ? actualInteractions / possibleInteractions : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
