package org.name.tool.core.metrics.cmr;

import org.name.tool.core.metrics.cm.CM;
import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.SecurityMetricValue;

public class CMRCached extends CMR {
    private final CMRImpl cmr;
    private SecurityMetricValue<Double> cachedResult;

    public CMRCached(CM cm) {
        this.cmr = new CMRImpl(cm);
        this.cachedResult = null;
    }

    @Override
    public SecurityMetricValue<Double> compute(ClassifiedAnalyzerResults classResults) {
        if (cachedResult == null) {
            cachedResult = cmr.compute(classResults);
        }
        return cachedResult;
    }
}
