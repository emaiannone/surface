package org.name.tool.core.metrics.ccva;

import org.name.tool.core.metrics.ca.CA;
import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.SecurityMetricValue;

public class CCVACached extends CCVA {
    private final CCVAImpl ccva;
    private SecurityMetricValue<Double> cachedResult;

    public CCVACached(CA ca) {
        this.ccva = new CCVAImpl(ca);
        this.cachedResult = null;
    }

    @Override
    public SecurityMetricValue<Double> compute(ClassifiedAnalyzerResults classResults) {
        if (cachedResult == null) {
            cachedResult = ccva.compute(classResults);
        }
        return cachedResult;
    }
}
