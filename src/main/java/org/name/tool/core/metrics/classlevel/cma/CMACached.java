package org.name.tool.core.metrics.classlevel.cma;

import org.name.tool.core.metrics.classlevel.cm.CM;
import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.MetricResult;

public class CMACached extends CMA {
    private final CMAImpl cma;
    private MetricResult<Double> cachedResult;

    public CMACached(CM cm) {
        this.cma = new CMAImpl(cm);
        this.cachedResult = null;
    }

    @Override
    public MetricResult<Double> compute(ClassifiedAnalyzerResults classResults) {
        if (cachedResult == null) {
            cachedResult = cma.compute(classResults);
        }
        return cachedResult;
    }
}
