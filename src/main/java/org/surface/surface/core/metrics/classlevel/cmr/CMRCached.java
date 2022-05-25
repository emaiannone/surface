package org.surface.surface.core.metrics.classlevel.cmr;

import org.surface.surface.core.inspection.results.ClassInspectorResults;
import org.surface.surface.core.metrics.classlevel.cm.CM;
import org.surface.surface.core.metrics.results.values.DoubleMetricValue;

public class CMRCached extends CMR {
    private final CMRImpl cmr;
    private DoubleMetricValue cachedResult;

    public CMRCached(CM cm) {
        this.cmr = new CMRImpl(cm);
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        if (cachedResult == null) {
            cachedResult = cmr.compute(classResults);
        }
        return cachedResult;
    }
}
