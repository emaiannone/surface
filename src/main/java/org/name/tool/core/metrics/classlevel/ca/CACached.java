package org.name.tool.core.metrics.classlevel.ca;

import org.name.tool.results.ClassifiedAnalyzerResults;
import org.name.tool.results.values.IntMetricValue;

public class CACached extends CA {
    private final CAImpl classifiedAttributes;
    private IntMetricValue cachedResult;

    public CACached() {
        this.classifiedAttributes = new CAImpl();
        this.cachedResult = null;
    }

    @Override
    public IntMetricValue compute(ClassifiedAnalyzerResults classResults) {
        if (cachedResult == null) {
            cachedResult = classifiedAttributes.compute(classResults);
        }
        return cachedResult;
    }
}
