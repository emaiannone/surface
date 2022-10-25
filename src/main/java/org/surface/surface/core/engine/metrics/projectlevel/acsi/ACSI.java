package org.surface.surface.core.engine.metrics.projectlevel.acsi;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class ACSI extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Average Critical Superclasses Inheritance";
    public static final String CODE = "A-CSI";

    ACSI() {
        setName(NAME);
        setCode(CODE);
    }
}
