package org.surface.surface.core.engine.metrics.clazz.cida;

import org.surface.surface.core.engine.metrics.api.ClassMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class CIDA extends ClassMetric<DoubleMetricValue> {
    public static final String NAME = "Classified Instance Data Accessibility (Class)";
    public static final String CODE = "CIDA";

    CIDA() {
        setName(NAME);
        setCode(CODE);
    }
}
