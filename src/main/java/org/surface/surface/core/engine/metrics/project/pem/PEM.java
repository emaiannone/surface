package org.surface.surface.core.engine.metrics.project.pem;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class PEM extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Economy of Mechanism";
    public static final String CODE = "PEM";

    PEM() {
        setName(NAME);
        setCode(CODE);
    }
}
