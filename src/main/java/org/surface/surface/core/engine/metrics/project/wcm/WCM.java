package org.surface.surface.core.engine.metrics.project.wcm;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class WCM extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Writability via Classified Methods";
    public static final String CODE = "WCM";

    WCM() {
        setName(NAME);
        setCode(CODE);
    }
}
