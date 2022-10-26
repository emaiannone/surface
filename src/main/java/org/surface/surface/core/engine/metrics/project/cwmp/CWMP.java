package org.surface.surface.core.engine.metrics.project.cwmp;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class CWMP extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Classified Writing Methods Proportion (Project)";
    public static final String CODE = "CWMP";

    CWMP() {
        setName(NAME);
        setCode(CODE);
    }
}