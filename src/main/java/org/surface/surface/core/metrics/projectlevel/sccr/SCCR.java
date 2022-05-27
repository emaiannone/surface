package org.surface.surface.core.metrics.projectlevel.sccr;

import org.surface.surface.core.metrics.api.ProjectMetric;
import org.surface.surface.core.metrics.results.values.DoubleMetricValue;

public abstract class SCCR extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Serializable Critical Classes Ratio";
    public static final String CODE = "SCCR";

    SCCR() {
        setName(NAME);
        setCode(CODE);
    }
}
