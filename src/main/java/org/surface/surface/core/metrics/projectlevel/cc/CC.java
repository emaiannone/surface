package org.surface.surface.core.metrics.projectlevel.cc;

import org.surface.surface.core.metrics.api.ProjectMetric;
import org.surface.surface.results.values.IntMetricValue;

public abstract class CC extends ProjectMetric<IntMetricValue> {
    public static final String NAME = "Critical Classes";
    public static final String CODE = "CC";

    protected CC() {
        setName(NAME);
        setCode(CODE);
    }
}
