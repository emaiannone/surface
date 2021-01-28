package org.name.tool.core.metrics.classlevel.ccva;

import org.name.tool.core.metrics.classlevel.ca.CA;
import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.SecurityMetricResult;

public class CCVACached extends CCVA {
    private final CCVAImpl ccva;
    private SecurityMetricResult<Double> cachedResult;

    public CCVACached(CA ca) {
        this.ccva = new CCVAImpl(ca);
        this.cachedResult = null;
    }

    @Override
    public SecurityMetricResult<Double> compute(ClassifiedAnalyzerResults classResults) {
        if (cachedResult == null) {
            cachedResult = ccva.compute(classResults);
        }
        return cachedResult;
    }
}
