package org.name.tool.core.metrics.ca;

import org.name.tool.core.analysis.results.ClassifiedAnalyzerResults;

public class ClassifiedAttributesCached implements ClassifiedAttributes {
    private final ClassifiedAttributesImpl classifiedAttributes;
    private Double cachedValue;

    public ClassifiedAttributesCached() {
        this.classifiedAttributes = new ClassifiedAttributesImpl();
        cachedValue = null;
    }

    @Override
    public double compute(ClassifiedAnalyzerResults classMap) {
        if (cachedValue == null) {
            cachedValue = classifiedAttributes.compute(classMap);
        }
        return cachedValue;
    }
}
