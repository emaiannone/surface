package org.surface.surface.core.engine.metrics.project.pras;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class PRAS extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Reduce Attack Surface";
    public static final String CODE = "PRAS";

    PRAS() {
        setName(NAME);
        setCode(CODE);
    }
}
