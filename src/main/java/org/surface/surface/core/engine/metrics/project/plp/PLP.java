package org.surface.surface.core.engine.metrics.project.plp;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class PLP extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Grant Least Privilege";
    public static final String CODE = "PLP";

    PLP() {
        setName(NAME);
        setCode(CODE);
    }
}
