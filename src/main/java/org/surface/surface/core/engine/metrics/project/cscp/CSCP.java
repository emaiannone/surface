package org.surface.surface.core.engine.metrics.project.cscp;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class CSCP extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Critical Serialized Classes Proportion";
    public static final String CODE = "CSCP";

    CSCP() {
        setName(NAME);
        setCode(CODE);
    }
}
