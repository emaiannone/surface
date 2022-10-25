package org.surface.surface.core.engine.metrics.project.ccda;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class CCDA extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Classified Class Data Accessibility (Project)";
    public static final String CODE = "CCDA";

    CCDA() {
        setName(NAME);
        setCode(CODE);
    }
}
