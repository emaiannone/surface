package org.surface.surface.core.metrics.projectlevel.cdp;

import org.surface.surface.core.metrics.api.ProjectMetric;
import org.surface.surface.core.metrics.results.values.DoubleMetricValue;

public abstract class CDP extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Critical Design Proportion";
    public static final String CODE = "CDP";

    CDP() {
        setName(NAME);
        setCode(CODE);
    }
}
