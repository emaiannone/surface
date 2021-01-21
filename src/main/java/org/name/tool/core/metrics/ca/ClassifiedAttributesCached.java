package org.name.tool.core.metrics.ca;

public class ClassifiedAttributesCached implements ClassifiedAttributes {
    private final ClassifiedAttributesImpl classifiedAttributes;
    private Double cachedValue;

    public ClassifiedAttributesCached() {
        this.classifiedAttributes = new ClassifiedAttributesImpl();
        cachedValue = null;
    }

    @Override
    public double compute() {
        if (cachedValue == null) {
            cachedValue = classifiedAttributes.compute();
        }
        return cachedValue;
    }
}
