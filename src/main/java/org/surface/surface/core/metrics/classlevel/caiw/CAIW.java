package org.surface.surface.core.metrics.classlevel.caiw;

import org.surface.surface.core.metrics.api.ClassMetric;
import org.surface.surface.core.metrics.results.values.DoubleMetricValue;

public abstract class CAIW extends ClassMetric<DoubleMetricValue> {
    public static final String NAME = "Classified Attributes Interaction Weight";
    public static final String CODE = "CAIW";

    CAIW() {
        setName(NAME);
        setCode(CODE);
    }
}