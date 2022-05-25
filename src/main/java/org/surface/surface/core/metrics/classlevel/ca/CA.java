package org.surface.surface.core.metrics.classlevel.ca;

import org.surface.surface.core.metrics.api.ClassMetric;
import org.surface.surface.core.metrics.results.values.IntMetricValue;

public abstract class CA extends ClassMetric<IntMetricValue> {
    public static final String NAME = "Classified Attributes";
    public static final String CODE = "CA";

    protected CA() {
        setName(NAME);
        setCode(CODE);
    }
}
