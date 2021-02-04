package org.name.tool.core.metrics.projectlevel.cscr;

import org.name.tool.core.metrics.api.ProjectMetric;
import org.name.tool.results.values.DoubleMetricValue;

public abstract class CSCR extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Critical SuperClasses Ratios";
    public static final String CODE = "CSCR";

    protected CSCR() {
        setName(NAME);
        setCode(CODE);
    }
}
