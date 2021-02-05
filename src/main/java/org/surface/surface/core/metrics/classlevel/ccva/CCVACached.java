package org.surface.surface.core.metrics.classlevel.ccva;

import org.surface.surface.core.metrics.classlevel.ca.CA;
import org.surface.surface.results.ClassifiedAnalyzerResults;
import org.surface.surface.results.values.DoubleMetricValue;

public class CCVACached extends CCVA {
    private final CCVAImpl ccva;
    private DoubleMetricValue cachedResult;

    public CCVACached(CA ca) {
        this.ccva = new CCVAImpl(ca);
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ClassifiedAnalyzerResults classResults) {
        if (cachedResult == null) {
            cachedResult = ccva.compute(classResults);
        }
        return cachedResult;
    }
}
