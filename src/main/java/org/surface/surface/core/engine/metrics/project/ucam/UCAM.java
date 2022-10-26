package org.surface.surface.core.engine.metrics.project.ucam;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class UCAM extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Uncalled Classified Accessor Method (Project)";
    public static final String CODE = "UCAM";

    UCAM() {
        setName(NAME);
        setCode(CODE);
    }
}
