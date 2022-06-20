package org.surface.surface.core.engine.metrics.classlevel.ccda;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.classlevel.cat.CAT;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CCDACached extends CCDA {
    private final CCDAImpl ccda;
    private DoubleMetricValue cachedResult;

    public CCDACached(CAT CAT) {
        this.ccda = new CCDAImpl(CAT);
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        if (cachedResult == null) {
            cachedResult = ccda.compute(classResults);
        }
        return cachedResult;
    }
}
