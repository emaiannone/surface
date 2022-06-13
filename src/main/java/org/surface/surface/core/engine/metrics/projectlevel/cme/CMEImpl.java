package org.surface.surface.core.engine.metrics.projectlevel.cme;

import com.github.javaparser.ast.body.MethodDeclaration;
import org.surface.surface.core.engine.inspection.results.ProjectInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

import java.util.Set;

public class CMEImpl extends CME {

    @Override
    public DoubleMetricValue compute(ProjectInspectorResults projectResults) {
        Set<MethodDeclaration> allClassifiedMethods = projectResults.getClassifiedMethods();
        int totalCM = allClassifiedMethods.size();
        long nonFinalCM = allClassifiedMethods.stream().filter(md -> !md.isFinal()).count();
        double value = totalCM != 0.0 ? (double) nonFinalCM / totalCM : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
