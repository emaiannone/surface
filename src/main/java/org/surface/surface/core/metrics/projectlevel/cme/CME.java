package org.surface.surface.core.metrics.projectlevel.cme;

import org.surface.surface.core.metrics.api.ProjectMetric;
import org.surface.surface.results.values.DoubleMetricValue;

public abstract class CME extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Critical Methods Extensibility";
    public static final String CODE = "CME";

    protected CME() {
        setName(NAME);
        setCode(CODE);
    }
}
