package org.name.tool.core.metrics.classlevel.ca;

import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.MetricResult;

public class CACached extends CA {
    private final CAImpl classifiedAttributes;
    private MetricResult<Integer> cachedResult;

    public CACached() {
        this.classifiedAttributes = new CAImpl();
        this.cachedResult = null;
    }

    @Override
    public MetricResult<Integer> compute(ClassifiedAnalyzerResults classResults) {
        if (cachedResult == null) {
            cachedResult = classifiedAttributes.compute(classResults);
        }
        return cachedResult;
    }
}
