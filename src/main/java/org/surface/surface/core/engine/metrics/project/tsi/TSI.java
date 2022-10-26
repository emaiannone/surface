package org.surface.surface.core.engine.metrics.project.tsi;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class TSI extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Total Security Index";
    public static final String CODE = "TSI";

    TSI() {
        setName(NAME);
        setCode(CODE);
    }
}
