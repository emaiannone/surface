package org.name.tool.core.metrics.classlevel.civa;

import org.name.tool.core.metrics.classlevel.ca.CA;
import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.MetricResult;

public class CIVACached extends CIVA {
    private final CIVAImpl civa;
    private MetricResult<Double> cachedResult;

    public CIVACached(CA ca) {
        this.civa = new CIVAImpl(ca);
        this.cachedResult = null;
    }

    @Override
    public MetricResult<Double> compute(ClassifiedAnalyzerResults classResults) {
        if (cachedResult == null) {
            cachedResult = civa.compute(classResults);
        }
        return cachedResult;
    }

}
