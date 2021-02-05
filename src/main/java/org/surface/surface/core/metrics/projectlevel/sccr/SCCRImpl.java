package org.surface.surface.core.metrics.projectlevel.sccr;

import org.surface.surface.core.metrics.projectlevel.cc.CC;
import org.surface.surface.results.ClassifiedAnalyzerResults;
import org.surface.surface.results.ProjectAnalyzerResults;
import org.surface.surface.results.values.DoubleMetricValue;

public class SCCRImpl extends SCCR {
    private final CC cc;

    public SCCRImpl(CC cc) {
        this.cc = cc;
    }

    @Override
    public DoubleMetricValue compute(ProjectAnalyzerResults projectResults) {
        int ccValue = cc.compute(projectResults).getValue();
        int serializedClassifiedClasses = 0;
        for (ClassifiedAnalyzerResults classResults : projectResults.getCriticalClasses()) {
            if (classResults.isSerializable()) {
                serializedClassifiedClasses++;
            }
        }
        double value = ccValue != 0.0 ? (double) serializedClassifiedClasses / ccValue : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
