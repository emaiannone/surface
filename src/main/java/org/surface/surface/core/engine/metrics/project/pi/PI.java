package org.surface.surface.core.engine.metrics.project.pi;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class PI extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Isolation";
    public static final String CODE = "PI";

    PI() {
        setName(NAME);
        setCode(CODE);
    }
}
