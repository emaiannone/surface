package org.surface.surface.core.metrics.classlevel.cai;

import org.surface.surface.core.inspection.results.ClassInspectorResults;
import org.surface.surface.core.metrics.classlevel.ca.CA;
import org.surface.surface.core.metrics.results.values.DoubleMetricValue;

public class CAICached extends CAI {
    private final CAIImpl cai;
    private DoubleMetricValue cachedResult;

    public CAICached(CA ca) {
        this.cai = new CAIImpl(ca);
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        if (cachedResult == null) {
            cachedResult = cai.compute(classResults);
        }
        return cachedResult;
    }
}
