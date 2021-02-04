package org.name.tool.core.metrics.classlevel.cma;

import com.github.javaparser.ast.body.MethodDeclaration;
import org.name.tool.core.metrics.classlevel.cm.CM;
import org.name.tool.results.ClassifiedAnalyzerResults;
import org.name.tool.results.MetricValue;

import java.util.Set;

public class CMAImpl extends CMA {
    private final CM cm;

    public CMAImpl(CM cm) {
        this.cm = cm;
    }

    @Override
    public MetricValue<Double> compute(ClassifiedAnalyzerResults classResults) {
        Set<MethodDeclaration> classifiedMethods = classResults.getClassifiedMethods();
        long nonPrivate = classifiedMethods.stream().filter(ca -> !ca.isPrivate()).count();
        int cmValue = cm.compute(classResults).getValue();
        double value = cmValue != 0.0 ? (double) nonPrivate / cmValue : 0.0;
        return new MetricValue<>(getName(), getCode(), value);
    }
}
