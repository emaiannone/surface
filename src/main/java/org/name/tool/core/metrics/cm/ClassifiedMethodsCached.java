package org.name.tool.core.metrics.cm;

import org.name.tool.core.analysis.results.ClassifiedAnalyzerResults;
import org.name.tool.core.metrics.api.SecurityMetricResult;

public class ClassifiedMethodsCached extends ClassifiedMethods {
    private final ClassifiedMethodsImpl classifiedMethods;
    private SecurityMetricResult cachedResult;

    public ClassifiedMethodsCached() {
        this.classifiedMethods = new ClassifiedMethodsImpl();
        this.cachedResult = null;
    }

    @Override
    public SecurityMetricResult compute(ClassifiedAnalyzerResults classResults) {
        if (cachedResult == null) {
            cachedResult = classifiedMethods.compute(classResults);
        }
        return cachedResult;
    }
}
