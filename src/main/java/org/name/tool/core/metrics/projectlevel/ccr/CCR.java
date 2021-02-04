package org.name.tool.core.metrics.projectlevel.ccr;

import org.name.tool.core.metrics.api.ProjectMetric;
import org.name.tool.results.values.DoubleMetricValue;

public abstract class CCR extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Critical Classes Ratio";
    public static final String CODE = "CCR";

    protected CCR() {
        setName(NAME);
        setCode(CODE);
    }
}
