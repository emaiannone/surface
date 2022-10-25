package org.surface.surface.core.engine.metrics.project.csi;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class CSI extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Critical Superclasses Inheritance";
    public static final String CODE = "CSI";

    CSI() {
        setName(NAME);
        setCode(CODE);
    }
}
