package org.name.tool.core.metrics.cc;

import org.name.tool.core.metrics.ca.CA;
import org.name.tool.core.metrics.cm.CM;
import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.SecurityMetricResult;

public class CCCached extends CC {
    private final CCImpl cc;
    private SecurityMetricResult<Boolean> cachedResult;

    public CCCached(CA ca, CM cm) {
        this.cc = new CCImpl(ca, cm);
        this.cachedResult = null;
    }

    @Override
    public SecurityMetricResult<Boolean> compute(ClassifiedAnalyzerResults classResults) {
        if (cachedResult == null) {
            cachedResult = cc.compute(classResults);
        }
        return cachedResult;
    }
}
