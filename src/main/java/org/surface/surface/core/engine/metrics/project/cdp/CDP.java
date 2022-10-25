package org.surface.surface.core.engine.metrics.project.cdp;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class CDP extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Critical Design Proportion";
    public static final String CODE = "CDP";

    CDP() {
        setName(NAME);
        setCode(CODE);
    }
}
