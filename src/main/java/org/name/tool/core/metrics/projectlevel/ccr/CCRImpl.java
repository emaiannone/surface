package org.name.tool.core.metrics.projectlevel.ccr;

import org.name.tool.core.metrics.projectlevel.cc.CC;
import org.name.tool.core.results.ProjectAnalyzerResults;
import org.name.tool.core.results.SecurityMetricResult;

public class CCRImpl extends CCR {
    private final CC cc;

    public CCRImpl(CC cc) {
        this.cc = cc;
    }

    @Override
    public SecurityMetricResult<Double> compute(ProjectAnalyzerResults projectResults) {
        int numClasses = projectResults.getResults().size();
        int ccValue = cc.compute(projectResults).getValue();
        double value = numClasses != 0.0 ? (double) ccValue / numClasses : 0.0;
        return new SecurityMetricResult<>(getName(), getCode(), value);
    }
}
