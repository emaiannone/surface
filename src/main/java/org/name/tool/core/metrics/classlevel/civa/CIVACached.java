package org.name.tool.core.metrics.classlevel.civa;

import org.name.tool.core.metrics.classlevel.ca.CA;
import org.name.tool.results.ClassifiedAnalyzerResults;
import org.name.tool.results.MetricValue;

public class CIVACached extends CIVA {
    private final CIVAImpl civa;
    private MetricValue<Double> cachedResult;

    public CIVACached(CA ca) {
        this.civa = new CIVAImpl(ca);
        this.cachedResult = null;
    }

    @Override
    public MetricValue<Double> compute(ClassifiedAnalyzerResults classResults) {
        if (cachedResult == null) {
            cachedResult = civa.compute(classResults);
        }
        return cachedResult;
    }

}
