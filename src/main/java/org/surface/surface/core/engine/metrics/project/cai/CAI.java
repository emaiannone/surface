package org.surface.surface.core.engine.metrics.project.cai;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class CAI extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Classified Attributes Inheritance";
    public static final String CODE = "CAI";

    CAI() {
        setName(NAME);
        setCode(CODE);
    }
}
