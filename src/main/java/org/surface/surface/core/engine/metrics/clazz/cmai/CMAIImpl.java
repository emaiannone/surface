package org.surface.surface.core.engine.metrics.clazz.cmai;

import com.github.javaparser.ast.body.VariableDeclarator;
import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CMAIImpl extends CMAI {

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        double actualInteractions = 0;
        for (VariableDeclarator attr : classResults.getClassifiedAttributes()) {
            actualInteractions += classResults.getNumberClassifiedMutators(attr);
        }
        double possibleInteractions = classResults.getNumberClassifiedAttributes() * classResults.getNumberClassifiedMutators();
        double value = possibleInteractions != 0.0 ? actualInteractions / possibleInteractions : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
