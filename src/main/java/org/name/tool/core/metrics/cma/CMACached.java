package org.name.tool.core.metrics.cma;

import org.name.tool.core.metrics.cm.CM;
import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.SecurityMetricValue;

public class CMACached extends CMA {
    private final CMAImpl cma;
    private SecurityMetricValue<Double> cachedResult;

    public CMACached(CM cm) {
        this.cma = new CMAImpl(cm);
        this.cachedResult = null;
    }

    @Override
    public SecurityMetricValue<Double> compute(ClassifiedAnalyzerResults classResults) {
        if (cachedResult == null) {
            cachedResult = cma.compute(classResults);
        }
        return cachedResult;
    }
}
