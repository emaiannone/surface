package org.surface.surface.core.engine.metrics.project.rcm;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class RCM extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Readability of Classified Methods";
    public static final String CODE = "RCM";

    RCM() {
        setName(NAME);
        setCode(CODE);
    }
}
