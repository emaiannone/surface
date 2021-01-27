package org.name.tool.core.metrics.cai;

import com.github.javaparser.ast.body.VariableDeclarator;
import org.name.tool.core.metrics.ca.CA;
import org.name.tool.core.metrics.cm.CM;
import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.SecurityMetricValue;

public class CAIImpl extends CAI {
    private final CA ca;
    private final CM cm;

    public CAIImpl(CA ca, CM cm) {
        this.ca = ca;
        this.cm = cm;
    }

    @Override
    public SecurityMetricValue compute(ClassifiedAnalyzerResults classResults) {
        double actualInteractions = 0;
        for (VariableDeclarator attr : classResults.getClassifiedAttributes()) {
            actualInteractions += classResults.getClassifiedMethods(attr).size();
        }
        double possibleInteractions = ca.compute(classResults).getValue() * cm.compute(classResults).getValue();
        double value = possibleInteractions != 0.0 ? actualInteractions / possibleInteractions : 0.0;
        return new SecurityMetricValue(getName(), getCode(), value);
    }
}
