package org.surface.surface.core.metrics.classlevel.cai;

import com.github.javaparser.ast.body.VariableDeclarator;
import org.surface.surface.core.metrics.classlevel.ca.CA;
import org.surface.surface.core.metrics.classlevel.cm.CM;
import org.surface.surface.results.ClassifiedAnalyzerResults;
import org.surface.surface.results.values.DoubleMetricValue;

public class CAIImpl extends CAI {
    private final CA ca;
    private final CM cm;

    public CAIImpl(CA ca, CM cm) {
        this.ca = ca;
        this.cm = cm;
    }

    @Override
    public DoubleMetricValue compute(ClassifiedAnalyzerResults classResults) {
        double actualInteractions = 0;
        for (VariableDeclarator attr : classResults.getClassifiedAttributes()) {
            actualInteractions += classResults.getClassifiedMethods(attr).size();
        }
        double possibleInteractions = ca.compute(classResults).getValue() * cm.compute(classResults).getValue();
        double value = possibleInteractions != 0.0 ? actualInteractions / possibleInteractions : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
