package org.surface.surface.core.engine.metrics.project.csp;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class CSP extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Critical Superclasses Proportion";
    public static final String CODE = "CSP";

    CSP() {
        setName(NAME);
        setCode(CODE);
    }
}
