package org.name.tool.core.metrics.cmr;

import org.name.tool.core.metrics.cm.CM;
import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.SecurityMetricValue;

public class CMRImpl extends CMR {
    private final CM cm;

    public CMRImpl(CM cm) {
        this.cm = cm;
    }

    @Override
    public SecurityMetricValue compute(ClassifiedAnalyzerResults classResults) {
        double cmValue = cm.compute(classResults).getValue();
        double methods = classResults.getMethods().size();
        double value = methods != 0.0 ? cmValue / methods : 0.0;
        return new SecurityMetricValue(getName(), getCode(), value);
    }
}
