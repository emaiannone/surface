package org.name.tool.core.metrics.ca;

import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.SecurityMetricValue;

public class CACached extends CA {
    private final CAImpl classifiedAttributes;
    private SecurityMetricValue<Integer> cachedResult;

    public CACached() {
        this.classifiedAttributes = new CAImpl();
        this.cachedResult = null;
    }

    @Override
    public SecurityMetricValue<Integer> compute(ClassifiedAnalyzerResults classResults) {
        if (cachedResult == null) {
            cachedResult = classifiedAttributes.compute(classResults);
        }
        return cachedResult;
    }
}
