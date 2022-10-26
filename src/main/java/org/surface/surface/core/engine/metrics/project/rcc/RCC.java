package org.surface.surface.core.engine.metrics.project.rcc;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class RCC extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Readability of Classified Classes";
    public static final String CODE = "RCC";

    RCC() {
        setName(NAME);
        setCode(CODE);
    }
}
