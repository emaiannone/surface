package org.name.tool.core.metrics.classlevel.cmr;

import org.name.tool.core.metrics.classlevel.cm.CM;
import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.MetricResult;

public class CMRCached extends CMR {
    private final CMRImpl cmr;
    private MetricResult<Double> cachedResult;

    public CMRCached(CM cm) {
        this.cmr = new CMRImpl(cm);
        this.cachedResult = null;
    }

    @Override
    public MetricResult<Double> compute(ClassifiedAnalyzerResults classResults) {
        if (cachedResult == null) {
            cachedResult = cmr.compute(classResults);
        }
        return cachedResult;
    }
}
