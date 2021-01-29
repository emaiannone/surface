package org.name.tool.core.metrics.classlevel.cm;

import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.MetricResult;

public class CMCached extends CM {
    private final CMImpl classifiedMethods;
    private MetricResult<Integer> cachedResult;

    public CMCached() {
        this.classifiedMethods = new CMImpl();
        this.cachedResult = null;
    }

    @Override
    public MetricResult<Integer> compute(ClassifiedAnalyzerResults classResults) {
        if (cachedResult == null) {
            cachedResult = classifiedMethods.compute(classResults);
        }
        return cachedResult;
    }
}
