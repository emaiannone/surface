package org.surface.surface.core.engine.metrics.project.sam;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class SAM extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Security Absolute Measurements";
    public static final String CODE = "SAM";

    SAM() {
        setName(NAME);
        setCode(CODE);
    }
}
