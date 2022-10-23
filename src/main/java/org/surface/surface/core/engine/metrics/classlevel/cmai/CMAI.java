package org.surface.surface.core.engine.metrics.classlevel.cmai;

import org.surface.surface.core.engine.metrics.api.ClassMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class CMAI extends ClassMetric<DoubleMetricValue> {
    public static final String NAME = "Classified Mutator Attribute Interactions";
    public static final String CODE = "CMAI";

    CMAI() {
        setName(NAME);
        setCode(CODE);
    }
}
