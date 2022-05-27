package org.surface.surface.core.metrics.classlevel.cmr;

import org.surface.surface.core.metrics.api.ClassMetric;
import org.surface.surface.core.metrics.results.values.DoubleMetricValue;

public abstract class CMR extends ClassMetric<DoubleMetricValue> {
    public static final String NAME = "Classified Methods Ratio";
    public static final String CODE = "CMR";

    CMR() {
        setName(NAME);
        setCode(CODE);
    }
}
