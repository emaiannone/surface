package org.name.tool.core.metrics.classlevel.cai;

import org.name.tool.core.metrics.classlevel.ca.CA;
import org.name.tool.core.metrics.classlevel.cm.CM;
import org.name.tool.results.ClassifiedAnalyzerResults;
import org.name.tool.results.MetricValue;

public class CAICached extends CAI {
    private final CAIImpl cai;
    private MetricValue<Double> cachedResult;

    public CAICached(CA ca, CM cm) {
        this.cai = new CAIImpl(ca, cm);
        this.cachedResult = null;
    }

    @Override
    public MetricValue<Double> compute(ClassifiedAnalyzerResults classResults) {
        if (cachedResult == null) {
            cachedResult = cai.compute(classResults);
        }
        return cachedResult;
    }
}
