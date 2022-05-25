package org.surface.surface.core.metrics.classlevel.cma;

import org.surface.surface.core.metrics.api.ClassMetric;
import org.surface.surface.core.metrics.results.values.DoubleMetricValue;

public abstract class CMA extends ClassMetric<DoubleMetricValue> {
    public static final String NAME = "Classified Method Accessibility";
    public static final String CODE = "CMA";

    protected CMA() {
        setName(NAME);
        setCode(CODE);
    }
}
