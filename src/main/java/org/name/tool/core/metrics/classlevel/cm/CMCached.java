package org.name.tool.core.metrics.classlevel.cm;

import org.name.tool.results.ClassifiedAnalyzerResults;
import org.name.tool.results.MetricValue;

public class CMCached extends CM {
    private final CMImpl classifiedMethods;
    private MetricValue<Integer> cachedResult;

    public CMCached() {
        this.classifiedMethods = new CMImpl();
        this.cachedResult = null;
    }

    @Override
    public MetricValue<Integer> compute(ClassifiedAnalyzerResults classResults) {
        if (cachedResult == null) {
            cachedResult = classifiedMethods.compute(classResults);
        }
        return cachedResult;
    }
}
