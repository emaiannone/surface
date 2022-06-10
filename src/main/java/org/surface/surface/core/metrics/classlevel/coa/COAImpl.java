package org.surface.surface.core.metrics.classlevel.coa;

import com.github.javaparser.ast.body.MethodDeclaration;
import org.surface.surface.core.inspection.results.ClassInspectorResults;
import org.surface.surface.core.metrics.classlevel.cmt.CMT;
import org.surface.surface.core.metrics.results.values.DoubleMetricValue;

import java.util.Set;

public class COAImpl extends COA {
    private final CMT CMT;

    public COAImpl(CMT CMT) {
        this.CMT = CMT;
    }

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        Set<MethodDeclaration> classifiedMethods = classResults.getAllClassifiedMethods();
        long nonPrivate = classifiedMethods.stream().filter(ca -> !ca.isPrivate()).count();
        int cmValue = CMT.compute(classResults).getValue();
        double value = cmValue != 0.0 ? (double) nonPrivate / cmValue : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
