package org.name.tool.core.metrics.civa;

import org.name.tool.core.analysis.results.ClassifiedAnalyzerResults;
import org.name.tool.core.metrics.api.SecurityMetricResult;
import org.name.tool.core.metrics.ca.ClassifiedAttributes;

public class ClassifiedInstanceVariablesAccessibilityCached extends ClassifiedInstanceVariablesAccessibility {
    private final ClassifiedInstanceVariablesAccessibilityImpl civa;
    private SecurityMetricResult cachedResult;

    public ClassifiedInstanceVariablesAccessibilityCached(ClassifiedAttributes ca) {
        this.civa = new ClassifiedInstanceVariablesAccessibilityImpl(ca);
        this.cachedResult = null;
    }

    @Override
    public SecurityMetricResult compute(ClassifiedAnalyzerResults classResults) {
        if (cachedResult == null) {
            cachedResult = civa.compute(classResults);
        }
        return cachedResult;
    }

}
