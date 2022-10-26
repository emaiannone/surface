package org.surface.surface.core.engine.metrics.project.pfsd;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class PFSD extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Fail-Safe Defaults";
    public static final String CODE = "PFSD";

    PFSD() {
        setName(NAME);
        setCode(CODE);
    }
}
