package org.surface.surface.core.engine.metrics.projectlevel.ucac;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class UCAC extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Unused Critical Accessor Class";
    public static final String CODE = "UCAC";

    UCAC() {
        setName(NAME);
        setCode(CODE);
    }
}
