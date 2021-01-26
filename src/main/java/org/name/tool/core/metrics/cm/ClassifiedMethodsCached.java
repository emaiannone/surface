package org.name.tool.core.metrics.cm;

import org.name.tool.core.analysis.results.ClassifiedAnalyzerResults;
import org.name.tool.core.metrics.api.SecurityMetricValue;

public class ClassifiedMethodsCached extends ClassifiedMethods {
    private final ClassifiedMethodsImpl classifiedMethods;
    private SecurityMetricValue cachedResult;

    public ClassifiedMethodsCached() {
        this.classifiedMethods = new ClassifiedMethodsImpl();
        this.cachedResult = null;
    }

    @Override
    public SecurityMetricValue compute(ClassifiedAnalyzerResults classResults) {
        if (cachedResult == null) {
            cachedResult = classifiedMethods.compute(classResults);
        }
        return cachedResult;
    }
}
