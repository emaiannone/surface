package org.surface.surface.core.metrics.classlevel.cm;

import org.surface.surface.core.metrics.api.ClassMetric;
import org.surface.surface.results.values.IntMetricValue;

public abstract class CM extends ClassMetric<IntMetricValue> {
    public static final String NAME = "Classified Methods";
    public static final String CODE = "CM";

    protected CM() {
        setName(NAME);
        setCode(CODE);
    }
}
