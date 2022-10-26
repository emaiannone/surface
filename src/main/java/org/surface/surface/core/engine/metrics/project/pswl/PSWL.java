package org.surface.surface.core.engine.metrics.project.pswl;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class PSWL extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Secure the Weakest Link";
    public static final String CODE = "PSWL";

    PSWL() {
        setName(NAME);
        setCode(CODE);
    }
}
