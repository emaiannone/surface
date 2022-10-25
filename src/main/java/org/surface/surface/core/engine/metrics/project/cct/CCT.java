package org.surface.surface.core.engine.metrics.project.cct;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.IntMetricValue;

public abstract class CCT extends ProjectMetric<IntMetricValue> {
    public static final String NAME = "Critical Classes Total";
    public static final String CODE = "CCT";

    CCT() {
        setName(NAME);
        setCode(CODE);
    }
}
