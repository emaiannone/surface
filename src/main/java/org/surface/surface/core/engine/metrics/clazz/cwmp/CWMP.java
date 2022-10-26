package org.surface.surface.core.engine.metrics.clazz.cwmp;

import org.surface.surface.core.engine.metrics.api.ClassMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class CWMP extends ClassMetric<DoubleMetricValue> {
    public static final String NAME = "Classified Writing Methods Proportion (Class)";
    public static final String CODE = "CWMP";

    CWMP() {
        setName(NAME);
        setCode(CODE);
    }
}