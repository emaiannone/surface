package org.name.tool.core.metrics.ca;

import org.name.tool.core.analysis.results.ClassifiedAnalyzerResults;
import org.name.tool.core.metrics.api.SecurityMetricResult;

public class ClassifiedAttributesCached extends ClassifiedAttributes {
    private final ClassifiedAttributesImpl classifiedAttributes;
    private SecurityMetricResult cachedResult;

    public ClassifiedAttributesCached() {
        this.classifiedAttributes = new ClassifiedAttributesImpl();
        this.cachedResult = null;
    }

    @Override
    public SecurityMetricResult compute(ClassifiedAnalyzerResults classResults) {
        if (cachedResult == null) {
            cachedResult = classifiedAttributes.compute(classResults);
        }
        return cachedResult;
    }
}
