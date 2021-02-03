package org.name.tool.core.metrics.projectlevel.sccr;

import org.name.tool.core.metrics.projectlevel.cc.CC;
import org.name.tool.results.ClassifiedAnalyzerResults;
import org.name.tool.results.MetricResult;
import org.name.tool.results.ProjectAnalyzerResults;

public class SCCRImpl extends SCCR {
    private final CC cc;

    public SCCRImpl(CC cc) {
        this.cc = cc;
    }

    @Override
    public MetricResult<Double> compute(ProjectAnalyzerResults projectResults) {
        int ccValue = cc.compute(projectResults).getValue();
        int serializedClassifiedClasses = 0;
        for (ClassifiedAnalyzerResults classResults : projectResults.getCriticalClasses()) {
            if (classResults.isSerializable()) {
                serializedClassifiedClasses++;
            }
        }
        double value = ccValue != 0.0 ? (double) serializedClassifiedClasses / ccValue : 0.0;
        return new MetricResult<>(getName(), getCode(), value);
    }
}
