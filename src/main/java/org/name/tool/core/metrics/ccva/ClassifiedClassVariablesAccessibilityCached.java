package org.name.tool.core.metrics.ccva;

import org.name.tool.core.metrics.ca.ClassifiedAttributes;
import org.name.tool.core.results.ClassifiedAnalyzerResults;
import org.name.tool.core.results.SecurityMetricValue;

public class ClassifiedClassVariablesAccessibilityCached extends ClassifiedClassVariablesAccessibility {
    private final ClassifiedClassVariablesAccessibilityImpl ccva;
    private SecurityMetricValue cachedResult;

    public ClassifiedClassVariablesAccessibilityCached(ClassifiedAttributes ca) {
        this.ccva = new ClassifiedClassVariablesAccessibilityImpl(ca);
        this.cachedResult = null;
    }

    @Override
    public SecurityMetricValue compute(ClassifiedAnalyzerResults classResults) {
        if (cachedResult == null) {
            cachedResult = ccva.compute(classResults);
        }
        return cachedResult;
    }
}
