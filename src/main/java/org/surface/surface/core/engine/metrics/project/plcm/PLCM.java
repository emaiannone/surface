package org.surface.surface.core.engine.metrics.project.plcm;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class PLCM extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Least Common Mechanism";
    public static final String CODE = "PLCM";

    PLCM() {
        setName(NAME);
        setCode(CODE);
    }
}
