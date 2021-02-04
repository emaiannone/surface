package org.name.tool.core.metrics.classlevel.rp;

import org.name.tool.results.ClassifiedAnalyzerResults;
import org.name.tool.results.MetricValue;

public class RPCached extends RP {
    private final RPImpl rp;
    private MetricValue<Boolean> cachedResult;

    public RPCached() {
        this.rp = new RPImpl();
        this.cachedResult = null;
    }

    @Override
    public MetricValue<Boolean> compute(ClassifiedAnalyzerResults classResults) {
        if (cachedResult == null) {
            cachedResult = rp.compute(classResults);
        }
        return cachedResult;
    }
}
