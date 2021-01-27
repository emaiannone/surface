package org.name.tool.core.metrics.cm;

import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.SecurityMetricValue;

public class CMCached extends CM {
    private final CMImpl classifiedMethods;
    private SecurityMetricValue cachedResult;

    public CMCached() {
        this.classifiedMethods = new CMImpl();
        this.cachedResult = null;
    }

    @Override
    public SecurityMetricValue compute(ClassifiedAnalyzerResults classResults) {
        if (cachedResult == null) {
            cachedResult = classifiedMethods.compute(classResults);
        }
        return cachedResult;
    }
}
