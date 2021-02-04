package org.name.tool.core.metrics.classlevel.cmr;

import org.name.tool.core.metrics.classlevel.cm.CM;
import org.name.tool.results.ClassifiedAnalyzerResults;
import org.name.tool.results.values.DoubleMetricValue;

public class CMRImpl extends CMR {
    private final CM cm;

    public CMRImpl(CM cm) {
        this.cm = cm;
    }

    @Override
    public DoubleMetricValue compute(ClassifiedAnalyzerResults classResults) {
        int cmValue = cm.compute(classResults).getValue();
        int methods = classResults.getMethods().size();
        double value = methods != 0.0 ? (double) cmValue / methods : 0.0;
        return new DoubleMetricValue(getName(), getCode(), value);
    }
}
