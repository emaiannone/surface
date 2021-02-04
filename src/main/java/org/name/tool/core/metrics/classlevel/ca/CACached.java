package org.name.tool.core.metrics.classlevel.ca;

import org.name.tool.results.ClassifiedAnalyzerResults;
import org.name.tool.results.MetricValue;

public class CACached extends CA {
    private final CAImpl classifiedAttributes;
    private MetricValue<Integer> cachedResult;

    public CACached() {
        this.classifiedAttributes = new CAImpl();
        this.cachedResult = null;
    }

    @Override
    public MetricValue<Integer> compute(ClassifiedAnalyzerResults classResults) {
        if (cachedResult == null) {
            cachedResult = classifiedAttributes.compute(classResults);
        }
        return cachedResult;
    }
}
