package org.name.tool.core.metrics.projectlevel.cme;

import com.github.javaparser.ast.body.MethodDeclaration;
import org.name.tool.results.MetricValue;
import org.name.tool.results.ProjectAnalyzerResults;

import java.util.Set;

public class CMEImpl extends CME {

    @Override
    public MetricValue<Double> compute(ProjectAnalyzerResults projectResults) {
        Set<MethodDeclaration> allClassifiedMethods = projectResults.getClassifiedMethods();
        int totalCM = allClassifiedMethods.size();
        long nonFinalCM = allClassifiedMethods.stream().filter(md -> !md.isFinal()).count();
        double value = totalCM != 0.0 ? (double) nonFinalCM / totalCM : 0.0;
        return new MetricValue<>(getName(), getCode(), value);
    }
}
