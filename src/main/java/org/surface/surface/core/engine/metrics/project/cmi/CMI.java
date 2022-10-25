package org.surface.surface.core.engine.metrics.project.cmi;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class CMI extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Classified Methods Inheritance";
    public static final String CODE = "CMI";

    CMI() {
        setName(NAME);
        setCode(CODE);
    }
}
