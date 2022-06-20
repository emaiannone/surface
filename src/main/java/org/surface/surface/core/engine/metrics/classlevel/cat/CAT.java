package org.surface.surface.core.engine.metrics.classlevel.cat;

import org.surface.surface.core.engine.metrics.api.ClassMetric;
import org.surface.surface.core.engine.metrics.results.values.IntMetricValue;

public abstract class CAT extends ClassMetric<IntMetricValue> {
    public static final String NAME = "Classified Attributes Total";
    public static final String CODE = "CAT";

    CAT() {
        setName(NAME);
        setCode(CODE);
    }
}
