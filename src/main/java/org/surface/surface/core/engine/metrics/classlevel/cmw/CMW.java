package org.surface.surface.core.engine.metrics.classlevel.cmw;

import org.surface.surface.core.engine.metrics.api.ClassMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class CMW extends ClassMetric<DoubleMetricValue> {
    public static final String NAME = "Classified Methods Weight";
    public static final String CODE = "CMW";

    CMW() {
        setName(NAME);
        setCode(CODE);
    }
}
