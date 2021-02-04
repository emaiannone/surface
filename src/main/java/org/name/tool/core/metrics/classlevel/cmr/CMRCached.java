package org.name.tool.core.metrics.classlevel.cmr;

import org.name.tool.core.metrics.classlevel.cm.CM;
import org.name.tool.results.ClassifiedAnalyzerResults;
import org.name.tool.results.MetricValue;

public class CMRCached extends CMR {
    private final CMRImpl cmr;
    private MetricValue<Double> cachedResult;

    public CMRCached(CM cm) {
        this.cmr = new CMRImpl(cm);
        this.cachedResult = null;
    }

    @Override
    public MetricValue<Double> compute(ClassifiedAnalyzerResults classResults) {
        if (cachedResult == null) {
            cachedResult = cmr.compute(classResults);
        }
        return cachedResult;
    }
}
