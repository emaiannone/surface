package org.surface.surface.core.metrics.projectlevel.cct;

import org.surface.surface.core.metrics.api.ProjectMetric;
import org.surface.surface.core.metrics.results.values.IntMetricValue;

public abstract class CCT extends ProjectMetric<IntMetricValue> {
    public static final String NAME = "Critical Classes Total";
    public static final String CODE = "CCT";

    CCT() {
        setName(NAME);
        setCode(CODE);
    }
}
