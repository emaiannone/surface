package org.surface.surface.core.engine.metrics.classlevel.ucam;

import org.surface.surface.core.engine.inspection.results.ClassInspectorResults;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public class UCAMCached extends UCAM {
    private final UCAMImpl ucam;
    private DoubleMetricValue cachedResult;

    public UCAMCached() {
        this.ucam = new UCAMImpl();
        this.cachedResult = null;
    }

    @Override
    public DoubleMetricValue compute(ClassInspectorResults classResults) {
        if (cachedResult == null) {
            cachedResult = ucam.compute(classResults);
        }
        return cachedResult;
    }
}
