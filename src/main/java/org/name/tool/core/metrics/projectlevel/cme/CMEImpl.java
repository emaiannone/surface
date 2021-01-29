package org.name.tool.core.metrics.projectlevel.cme;

import com.github.javaparser.ast.body.MethodDeclaration;
import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.ProjectAnalyzerResults;
import org.name.tool.core.results.SecurityMetricResult;

import java.util.HashSet;
import java.util.Set;

public class CMEImpl extends CME {

    @Override
    public SecurityMetricResult<Double> compute(ProjectAnalyzerResults projectResults) {
        Set<MethodDeclaration> allClassifiedMethods = new HashSet<>();
        for (ClassifiedAnalyzerResults classResults : projectResults) {
            allClassifiedMethods.addAll(new HashSet<>(classResults.getClassifiedMethods()));
        }
        int totalCM = allClassifiedMethods.size();
        long nonFinalCM = allClassifiedMethods.stream().filter(md -> !md.isFinal()).count();
        double value = totalCM != 0.0 ? (double) nonFinalCM / totalCM : 0.0;
        return new SecurityMetricResult<>(getName(), getCode(), value);
    }
}
