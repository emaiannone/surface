package org.name.tool.core.metrics.ca;

import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.SecurityMetricValue;

public class ClassifiedAttributesCached extends ClassifiedAttributes {
    private final ClassifiedAttributesImpl classifiedAttributes;
    private SecurityMetricValue cachedResult;

    public ClassifiedAttributesCached() {
        this.classifiedAttributes = new ClassifiedAttributesImpl();
        this.cachedResult = null;
    }

    @Override
    public SecurityMetricValue compute(ClassifiedAnalyzerResults classResults) {
        if (cachedResult == null) {
            cachedResult = classifiedAttributes.compute(classResults);
        }
        return cachedResult;
    }
}
