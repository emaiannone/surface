package org.surface.surface.core.engine.metrics.project.rca;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class RCA extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Readability via Classified Attributes";
    public static final String CODE = "RCA";

    RCA() {
        setName(NAME);
        setCode(CODE);
    }
}
