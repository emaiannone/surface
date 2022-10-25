package org.surface.surface.core.engine.metrics.clazz.coa;

import org.surface.surface.core.engine.metrics.api.ClassMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class COA extends ClassMetric<DoubleMetricValue> {
    public static final String NAME = "Classified Operation Accessibility (Class)";
    public static final String CODE = "COA";

    COA() {
        setName(NAME);
        setCode(CODE);
    }
}
