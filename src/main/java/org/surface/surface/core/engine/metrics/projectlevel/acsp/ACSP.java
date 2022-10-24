package org.surface.surface.core.engine.metrics.projectlevel.acsp;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class ACSP extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Average Critical Superclasses Proportion";
    public static final String CODE = "A-CSP";

    ACSP() {
        setName(NAME);
        setCode(CODE);
    }
}
