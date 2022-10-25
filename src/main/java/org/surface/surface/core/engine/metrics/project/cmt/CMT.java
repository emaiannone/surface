package org.surface.surface.core.engine.metrics.project.cmt;

import org.surface.surface.core.engine.metrics.api.ProjectMetric;
import org.surface.surface.core.engine.metrics.results.values.IntMetricValue;

public abstract class CMT extends ProjectMetric<IntMetricValue> {
    public static final String NAME = "Classified Methods Total (Project)";
    public static final String CODE = "CMT";

    CMT() {
        setName(NAME);
        setCode(CODE);
    }
}
