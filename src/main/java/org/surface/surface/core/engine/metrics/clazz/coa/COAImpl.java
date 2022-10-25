package org.surface.surface.core.engine.metrics.clazz.coa;

import com.github.javaparser.ast.body.MethodDeclaration;
import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

import java.util.Set;

public class COAImpl extends COA {

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        Set<MethodDeclaration> classifiedMethods = classResults.getClassifiedMethods();
        long nonPrivate = classifiedMethods.stream().filter(ca -> !ca.isPrivate()).count();
        int cmtValue = classResults.getNumberClassifiedMethods();
        double value = cmtValue != 0.0 ? (double) nonPrivate / cmtValue : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
