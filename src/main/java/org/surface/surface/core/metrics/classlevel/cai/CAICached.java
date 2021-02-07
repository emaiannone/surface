package org.surface.surface.core.metrics.classlevel.cai;

import org.surface.surface.core.metrics.classlevel.ca.CA;
import org.surface.surface.core.metrics.classlevel.cm.CM;
import org.surface.surface.results.ClassifiedAnalyzerResults;
import org.surface.surface.results.values.DoubleMetricValue;

public class CAICached extends CAI {
    private final CAIImpl cai;
    private DoubleMetricValue cachedResult;

    public CAICached(CA ca) {
        this.cai = new CAIImpl(ca);
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ClassifiedAnalyzerResults classResults) {
        if (cachedResult == null) {
            cachedResult = cai.compute(classResults);
        }
        return cachedResult;
    }
}
