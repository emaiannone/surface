package org.name.tool.core.metrics.projectlevel.cc;

import org.name.tool.core.metrics.api.ProjectMetric;
import org.name.tool.results.values.IntMetricValue;

public abstract class CC extends ProjectMetric<IntMetricValue> {
    public static final String NAME = "Critical Classes";
    public static final String CODE = "CC";

    protected CC() {
        setName(NAME);
        setCode(CODE);
    }
}
