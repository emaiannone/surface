package org.surface.surface.core.metrics.classlevel.cai;

import org.surface.surface.core.metrics.api.ClassMetric;
import org.surface.surface.core.metrics.results.values.DoubleMetricValue;

public abstract class CAI extends ClassMetric<DoubleMetricValue> {
    public static final String NAME = "Classified Attributes Interactions";
    public static final String CODE = "CAI";

    CAI() {
        setName(NAME);
        setCode(CODE);
    }
}