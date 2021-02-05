package org.surface.surface.core.metrics.classlevel.civa;

import org.surface.surface.core.metrics.classlevel.ca.CA;
import org.surface.surface.results.ClassifiedAnalyzerResults;
import org.surface.surface.results.values.DoubleMetricValue;

public class CIVACached extends CIVA {
    private final CIVAImpl civa;
    private DoubleMetricValue cachedResult;

    public CIVACached(CA ca) {
        this.civa = new CIVAImpl(ca);
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ClassifiedAnalyzerResults classResults) {
        if (cachedResult == null) {
            cachedResult = civa.compute(classResults);
        }
        return cachedResult;
    }

}
