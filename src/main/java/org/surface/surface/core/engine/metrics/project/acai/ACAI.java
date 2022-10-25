package org.surface.surface.core.engine.metrics.project.acai;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class ACAI extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Average Classified Attributes Inheritance";
    public static final String CODE = "A-CAI";

    ACAI() {
        setName(NAME);
        setCode(CODE);
    }
}
