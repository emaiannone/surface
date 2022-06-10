package org.surface.surface.core.metrics.classlevel.cida;

import org.surface.surface.core.inspection.results.ClassInspectorResults;
import org.surface.surface.core.metrics.classlevel.cat.CAT;
import org.surface.surface.core.metrics.results.values.DoubleMetricValue;

public class CIDACached extends CIDA {
    private final CIDAImpl cida;
    private DoubleMetricValue cachedResult;

    public CIDACached(CAT CAT) {
        this.cida = new CIDAImpl(CAT);
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        if (cachedResult == null) {
            cachedResult = cida.compute(classResults);
        }
        return cachedResult;
    }

}
