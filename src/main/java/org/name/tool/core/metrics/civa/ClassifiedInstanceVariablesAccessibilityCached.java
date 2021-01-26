package org.name.tool.core.metrics.civa;

import org.name.tool.core.analysis.results.ClassifiedAnalyzerResults;
import org.name.tool.core.metrics.api.SecurityMetricValue;
import org.name.tool.core.metrics.ca.ClassifiedAttributes;

public class ClassifiedInstanceVariablesAccessibilityCached extends ClassifiedInstanceVariablesAccessibility {
    private final ClassifiedInstanceVariablesAccessibilityImpl civa;
    private SecurityMetricValue cachedResult;

    public ClassifiedInstanceVariablesAccessibilityCached(ClassifiedAttributes ca) {
        this.civa = new ClassifiedInstanceVariablesAccessibilityImpl(ca);
        this.cachedResult = null;
    }

    @Override
    public SecurityMetricValue compute(ClassifiedAnalyzerResults classResults) {
        if (cachedResult == null) {
            cachedResult = civa.compute(classResults);
        }
        return cachedResult;
    }

}
