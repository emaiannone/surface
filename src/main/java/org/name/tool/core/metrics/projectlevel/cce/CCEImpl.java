package org.name.tool.core.metrics.projectlevel.cce;

import org.name.tool.core.metrics.projectlevel.cc.CC;
import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.ProjectAnalyzerResults;
import org.name.tool.core.results.SecurityMetricResult;

public class CCEImpl extends CCE {
    private final CC cc;

    public CCEImpl(CC cc) {
        this.cc = cc;
    }

    @Override
    public SecurityMetricResult<Double> compute(ProjectAnalyzerResults projectResults) {
        int nonFinalCC = 0;
        for (ClassifiedAnalyzerResults classResults : projectResults) {
            if (classResults.isCritical() && !classResults.isFinal()) {
                nonFinalCC++;
            }
        }
        int ccValue = cc.compute(projectResults).getValue();
        double value = ccValue != 0.0 ? (double) nonFinalCC / ccValue : 0.0;
        return new SecurityMetricResult<>(getName(), getCode(), value);
    }
}
