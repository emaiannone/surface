package org.surface.surface.core.engine.metrics.project.cmai;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class CMAI extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Classified Mutator Attribute Interactions (Project)";
    public static final String CODE = "CMAI";

    CMAI() {
        setName(NAME);
        setCode(CODE);
    }
}
