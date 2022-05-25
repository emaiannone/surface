package org.surface.surface.core.metrics.classlevel.cai;

import com.github.javaparser.ast.body.VariableDeclarator;
import org.surface.surface.core.inspection.results.ClassInspectorResults;
import org.surface.surface.core.metrics.classlevel.ca.CA;
import org.surface.surface.core.metrics.results.values.DoubleMetricValue;

public class CAIImpl extends CAI {
    private final CA ca;

    public CAIImpl(CA ca) {
        this.ca = ca;
    }

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        double actualInteractions = 0;
        for (VariableDeclarator attr : classResults.getClassifiedAttributes()) {
            actualInteractions += classResults.getUsageClassifiedMethods(attr).size();
        }
        double possibleInteractions = ca.compute(classResults).getValue() * classResults.getUsageClassifiedMethods().size();
        double value = possibleInteractions != 0.0 ? actualInteractions / possibleInteractions : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
