package org.surface.surface.core.engine.metrics.projectlevel.ccc;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class CCC extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Critical Classes Coupling";
    public static final String CODE = "CCC";

    CCC() {
        setName(NAME);
        setCode(CODE);
    }
}
