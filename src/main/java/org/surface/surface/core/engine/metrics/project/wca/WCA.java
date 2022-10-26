package org.surface.surface.core.engine.metrics.project.wca;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class WCA extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Writability of Classified Attributes";
    public static final String CODE = "WCA";

    WCA() {
        setName(NAME);
        setCode(CODE);
    }
}
