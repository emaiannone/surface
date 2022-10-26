package org.surface.surface.core.engine.metrics.project.wcc;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class WCC extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Writability of Classified Classes";
    public static final String CODE = "WCC";

    WCC() {
        setName(NAME);
        setCode(CODE);
    }
}
