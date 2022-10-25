package org.surface.surface.core.engine.metrics.project.cida;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class CIDA extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Classified Instance Data Accessibility (Project)";
    public static final String CODE = "CIDA";

    CIDA() {
        setName(NAME);
        setCode(CODE);
    }
}
