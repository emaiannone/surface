package org.surface.surface.core.engine.metrics.projectlevel.acmi;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class ACMI extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Average Classified Methods Inheritance";
    public static final String CODE = "A-CMI";

    ACMI() {
        setName(NAME);
        setCode(CODE);
    }
}
