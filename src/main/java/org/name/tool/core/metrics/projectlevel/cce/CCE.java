package org.name.tool.core.metrics.projectlevel.cce;

import org.name.tool.core.metrics.api.ProjectMetric;
import org.name.tool.results.values.DoubleMetricValue;

public abstract class CCE extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Critical Classes Extensibility";
    public static final String CODE = "CCE";

    protected CCE() {
        setName(NAME);
        setCode(CODE);
    }
}
