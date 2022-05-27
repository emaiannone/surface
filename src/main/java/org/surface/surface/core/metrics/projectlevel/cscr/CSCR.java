package org.surface.surface.core.metrics.projectlevel.cscr;

import org.surface.surface.core.metrics.api.ProjectMetric;
import org.surface.surface.core.metrics.results.values.DoubleMetricValue;

public abstract class CSCR extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Critical SuperClasses Ratios";
    public static final String CODE = "CSCR";

    CSCR() {
        setName(NAME);
        setCode(CODE);
    }
}
