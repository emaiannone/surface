package org.name.tool.core.metrics.projectlevel.cce;

import org.name.tool.core.metrics.projectlevel.cc.CC;
import org.name.tool.results.ClassifiedAnalyzerResults;
import org.name.tool.results.MetricValue;
import org.name.tool.results.ProjectAnalyzerResults;

public class CCEImpl extends CCE {
    private final CC cc;

    public CCEImpl(CC cc) {
        this.cc = cc;
    }

    @Override
    public MetricValue<Double> compute(ProjectAnalyzerResults projectResults) {
        int nonFinalCC = 0;
        for (ClassifiedAnalyzerResults classResults : projectResults) {
            if (classResults.isCritical() && !classResults.isFinal()) {
                nonFinalCC++;
            }
        }
        int ccValue = cc.compute(projectResults).getValue();
        double value = ccValue != 0.0 ? (double) nonFinalCC / ccValue : 0.0;
        return new MetricValue<>(getName(), getCode(), value);
    }
}
