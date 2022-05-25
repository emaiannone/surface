package org.surface.surface.core.metrics.projectlevel.ccr;

import org.surface.surface.core.metrics.api.ProjectMetric;
import org.surface.surface.core.metrics.results.values.DoubleMetricValue;

public abstract class CCR extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Critical Classes Ratio";
    public static final String CODE = "CCR";

    protected CCR() {
        setName(NAME);
        setCode(CODE);
    }
}
