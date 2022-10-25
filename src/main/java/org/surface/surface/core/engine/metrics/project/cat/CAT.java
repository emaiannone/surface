package org.surface.surface.core.engine.metrics.project.cat;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.IntMetricValue;

public abstract class CAT extends ProjectMetric<IntMetricValue> {
    public static final String NAME = "Classified Attributes Total (Project)";
    public static final String CODE = "CAT";

    CAT() {
        setName(NAME);
        setCode(CODE);
    }
}
