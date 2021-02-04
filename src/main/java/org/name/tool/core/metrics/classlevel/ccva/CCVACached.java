package org.name.tool.core.metrics.classlevel.ccva;

import org.name.tool.core.metrics.classlevel.ca.CA;
import org.name.tool.results.ClassifiedAnalyzerResults;
import org.name.tool.results.MetricValue;

public class CCVACached extends CCVA {
    private final CCVAImpl ccva;
    private MetricValue<Double> cachedResult;

    public CCVACached(CA ca) {
        this.ccva = new CCVAImpl(ca);
        this.cachedResult = null;
    }

    @Override
    public MetricValue<Double> compute(ClassifiedAnalyzerResults classResults) {
        if (cachedResult == null) {
            cachedResult = ccva.compute(classResults);
        }
        return cachedResult;
    }
}
