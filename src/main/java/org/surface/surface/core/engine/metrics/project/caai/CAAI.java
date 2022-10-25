package org.surface.surface.core.engine.metrics.project.caai;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class CAAI extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Classified Accessor Attribute Interactions (Project)";
    public static final String CODE = "CAAI";

    CAAI() {
        setName(NAME);
        setCode(CODE);
    }
}
