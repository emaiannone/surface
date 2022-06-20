package org.surface.surface.core.engine.metrics.classlevel.cmw;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.classlevel.cmt.CMT;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CMWCached extends CMW {
    private final CMWImpl cmw;
    private DoubleMetricValue cachedResult;

    public CMWCached(CMT CMT) {
        this.cmw = new CMWImpl(CMT);
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        if (cachedResult == null) {
            cachedResult = cmw.compute(classResults);
        }
        return cachedResult;
    }
}
