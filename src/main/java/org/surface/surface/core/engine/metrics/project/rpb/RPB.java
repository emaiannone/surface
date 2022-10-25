package org.surface.surface.core.engine.metrics.project.rpb;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.BooleanMetricValue;

public abstract class RPB extends ProjectMetric<BooleanMetricValue> {
    public static final String NAME = "Reflection Package Boolean (Project)";
    public static final String CODE = "RPB";

    RPB() {
        setName(NAME);
        setCode(CODE);
    }
}
