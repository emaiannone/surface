package org.surface.surface.core.engine.metrics.project.coa;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class COA extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Classified Operation Accessibility (Project)";
    public static final String CODE = "COA";

    COA() {
        setName(NAME);
        setCode(CODE);
    }
}
