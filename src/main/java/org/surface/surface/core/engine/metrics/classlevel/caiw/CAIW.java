package org.surface.surface.core.engine.metrics.classlevel.caiw;

import org.surface.surface.core.engine.metrics.api.ClassMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class CAIW extends ClassMetric<DoubleMetricValue> {
    public static final String NAME = "Classified Attributes Interaction Weight";
    public static final String CODE = "CAIW";

    CAIW() {
        setName(NAME);
        setCode(CODE);
    }
}