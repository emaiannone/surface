package org.name.tool.core.metrics.classlevel.rp;

import org.name.tool.results.ClassifiedAnalyzerResults;
import org.name.tool.results.MetricResult;

public class RPCached extends RP {
    private final RPImpl rp;
    private MetricResult<Boolean> cachedResult;

    public RPCached() {
        this.rp = new RPImpl();
        this.cachedResult = null;
    }

    @Override
    public MetricResult<Boolean> compute(ClassifiedAnalyzerResults classResults) {
        if (cachedResult == null) {
            cachedResult = rp.compute(classResults);
        }
        return cachedResult;
    }
}
