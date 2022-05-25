package org.surface.surface.core.metrics.classlevel.ccva;

import org.surface.surface.core.inspection.results.ClassInspectorResults;
import org.surface.surface.core.metrics.classlevel.ca.CA;
import org.surface.surface.core.metrics.results.values.DoubleMetricValue;

public class CCVACached extends CCVA {
    private final CCVAImpl ccva;
    private DoubleMetricValue cachedResult;

    public CCVACached(CA ca) {
        this.ccva = new CCVAImpl(ca);
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        if (cachedResult == null) {
            cachedResult = ccva.compute(classResults);
        }
        return cachedResult;
    }
}
