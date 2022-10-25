package org.surface.surface.core.engine.metrics.project.cpcc;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class CPCC extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Composite-Part Critical Classes";
    public static final String CODE = "CPCC";

    CPCC() {
        setName(NAME);
        setCode(CODE);
    }
}
