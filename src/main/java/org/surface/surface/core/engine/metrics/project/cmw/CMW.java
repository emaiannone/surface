package org.surface.surface.core.engine.metrics.project.cmw;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class CMW extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Classified Methods Weight (Project)";
    public static final String CODE = "CMW";

    CMW() {
        setName(NAME);
        setCode(CODE);
    }
}
