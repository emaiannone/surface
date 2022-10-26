package org.surface.surface.core.engine.metrics.project.uaca;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class UACA extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Unaccessed Assigned Classified Attribute (Project)";
    public static final String CODE = "UACA";

    UACA() {
        setName(NAME);
        setCode(CODE);
    }
}
