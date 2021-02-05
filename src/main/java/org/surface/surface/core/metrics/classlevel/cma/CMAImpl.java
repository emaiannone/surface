package org.surface.surface.core.metrics.classlevel.cma;

import com.github.javaparser.ast.body.MethodDeclaration;
import org.surface.surface.core.metrics.classlevel.cm.CM;
import org.surface.surface.results.ClassifiedAnalyzerResults;
import org.surface.surface.results.values.DoubleMetricValue;

import java.util.Set;

public class CMAImpl extends CMA {
    private final CM cm;

    public CMAImpl(CM cm) {
        this.cm = cm;
    }

    @Override
    public DoubleMetricValue compute(ClassifiedAnalyzerResults classResults) {
        Set<MethodDeclaration> classifiedMethods = classResults.getClassifiedMethods();
        long nonPrivate = classifiedMethods.stream().filter(ca -> !ca.isPrivate()).count();
        int cmValue = cm.compute(classResults).getValue();
        double value = cmValue != 0.0 ? (double) nonPrivate / cmValue : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
