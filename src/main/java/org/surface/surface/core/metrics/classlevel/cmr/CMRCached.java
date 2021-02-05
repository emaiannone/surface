package org.surface.surface.core.metrics.classlevel.cmr;

import org.surface.surface.core.metrics.classlevel.cm.CM;
import org.surface.surface.results.ClassifiedAnalyzerResults;
import org.surface.surface.results.values.DoubleMetricValue;

public class CMRCached extends CMR {
    private final CMRImpl cmr;
    private DoubleMetricValue cachedResult;

    public CMRCached(CM cm) {
        this.cmr = new CMRImpl(cm);
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ClassifiedAnalyzerResults classResults) {
        if (cachedResult == null) {
            cachedResult = cmr.compute(classResults);
        }
        return cachedResult;
    }
}
