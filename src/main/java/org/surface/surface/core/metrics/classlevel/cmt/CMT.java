package org.surface.surface.core.metrics.classlevel.cmt;

import org.surface.surface.core.metrics.api.ClassMetric;
import org.surface.surface.core.metrics.results.values.IntMetricValue;

public abstract class CMT extends ClassMetric<IntMetricValue> {
    public static final String NAME = "Classified Methods Total";
    public static final String CODE = "CMT";

    CMT() {
        setName(NAME);
        setCode(CODE);
    }
}
