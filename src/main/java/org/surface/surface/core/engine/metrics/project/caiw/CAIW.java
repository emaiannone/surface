package org.surface.surface.core.engine.metrics.project.caiw;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class CAIW extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Classified Attributes Interaction Weight (Project)";
    public static final String CODE = "CAIW";

    CAIW() {
        setName(NAME);
        setCode(CODE);
    }
}