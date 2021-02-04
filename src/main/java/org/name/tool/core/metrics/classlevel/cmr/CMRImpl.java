package org.name.tool.core.metrics.classlevel.cmr;

import org.name.tool.core.metrics.classlevel.cm.CM;
import org.name.tool.results.ClassifiedAnalyzerResults;
import org.name.tool.results.MetricValue;

public class CMRImpl extends CMR {
    private final CM cm;

    public CMRImpl(CM cm) {
        this.cm = cm;
    }

    @Override
    public MetricValue<Double> compute(ClassifiedAnalyzerResults classResults) {
        int cmValue = cm.compute(classResults).getValue();
        int methods = classResults.getMethods().size();
        double value = methods != 0.0 ? (double) cmValue / methods : 0.0;
        return new MetricValue<>(getName(), getCode(), value);
    }
}
