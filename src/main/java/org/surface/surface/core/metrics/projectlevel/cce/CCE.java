package org.surface.surface.core.metrics.projectlevel.cce;

import org.surface.surface.core.metrics.api.ProjectMetric;
import org.surface.surface.core.metrics.results.values.DoubleMetricValue;

public abstract class CCE extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Critical Classes Extensibility";
    public static final String CODE = "CCE";

    CCE() {
        setName(NAME);
        setCode(CODE);
    }
}
