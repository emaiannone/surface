package org.surface.surface.core.engine.metrics.clazz.cmt;

import org.surface.surface.core.engine.metrics.api.ClassMetric;
import org.surface.surface.core.engine.metrics.results.values.IntMetricValue;

public abstract class CMT extends ClassMetric<IntMetricValue> {
    public static final String NAME = "Classified Methods Total (Class)";
    public static final String CODE = "CMT";

    CMT() {
        setName(NAME);
        setCode(CODE);
    }
}
