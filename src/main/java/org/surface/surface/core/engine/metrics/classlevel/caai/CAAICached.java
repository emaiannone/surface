package org.surface.surface.core.engine.metrics.classlevel.caai;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.classlevel.cat.CAT;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class CAAICached extends CAAI {
    private final CAAIImpl caai;
    private DoubleMetricValue cachedResult;

    public CAAICached(CAT cat) {
        this.caai = new CAAIImpl(cat);
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        if (cachedResult == null) {
            cachedResult = caai.compute(classResults);
        }
        return cachedResult;
    }
}
