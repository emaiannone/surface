package org.surface.surface.core.metrics.projectlevel.cme;

import org.surface.surface.core.metrics.api.ProjectMetric;
import org.surface.surface.core.metrics.results.values.DoubleMetricValue;

public abstract class CME extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Classified Methods Extensibility";
    public static final String CODE = "CME";

    CME() {
        setName(NAME);
        setCode(CODE);
    }
}
