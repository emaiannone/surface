package org.surface.surface.core.engine.metrics.clazz.ucam;

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
    public DoubleMetricValue compute(ClassInspectorResults classResult) {
        if (cachedResult == null) {
            cachedResult = ucam.compute(classResult);
        }
        return cachedResult;
    }
}
