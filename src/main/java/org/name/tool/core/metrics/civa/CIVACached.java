package org.name.tool.core.metrics.civa;

import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.SecurityMetricResult;
import org.name.tool.core.metrics.ca.CA;

public class CIVACached extends CIVA {
    private final CIVAImpl civa;
    private SecurityMetricResult<Double> cachedResult;

    public CIVACached(CA ca) {
        this.civa = new CIVAImpl(ca);
        this.cachedResult = null;
    }

    @Override
    public SecurityMetricResult<Double> compute(ClassifiedAnalyzerResults classResults) {
        if (cachedResult == null) {
            cachedResult = civa.compute(classResults);
        }
        return cachedResult;
    }

}
