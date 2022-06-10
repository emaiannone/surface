package org.surface.surface.core.metrics.classlevel.rpb;

import org.surface.surface.core.metrics.api.ClassMetric;
import org.surface.surface.core.metrics.results.values.BooleanMetricValue;

public abstract class RPB extends ClassMetric<BooleanMetricValue> {
    public static final String NAME = "Reflection Package Boolean";
    public static final String CODE = "RPB";

    RPB() {
        setName(NAME);
        setCode(CODE);
    }
}
