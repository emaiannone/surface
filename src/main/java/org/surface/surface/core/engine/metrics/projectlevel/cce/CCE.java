package org.surface.surface.core.engine.metrics.projectlevel.cce;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class CCE extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Critical Classes Extensibility";
    public static final String CODE = "CCE";

    CCE() {
        setName(NAME);
        setCode(CODE);
    }
}
