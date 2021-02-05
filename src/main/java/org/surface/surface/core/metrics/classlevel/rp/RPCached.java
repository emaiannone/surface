package org.surface.surface.core.metrics.classlevel.rp;

import org.surface.surface.results.ClassifiedAnalyzerResults;
import org.surface.surface.results.values.BooleanMetricValue;

public class RPCached extends RP {
    private final RPImpl rp;
    private BooleanMetricValue cachedResult;

    public RPCached() {
        this.rp = new RPImpl();
        this.cachedResult = null;
    }

    @Override
    public BooleanMetricValue compute(ClassifiedAnalyzerResults classResults) {
        if (cachedResult == null) {
            cachedResult = rp.compute(classResults);
        }
        return cachedResult;
    }
}
