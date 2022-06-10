package org.surface.surface.core.metrics.classlevel.ccda;

import org.surface.surface.core.metrics.api.ClassMetric;
import org.surface.surface.core.metrics.results.values.DoubleMetricValue;

public abstract class CCDA extends ClassMetric<DoubleMetricValue> {
    private static final String NAME = "Classified Class Data Accessibility";
    public static final String CODE = "CCDA";

    CCDA() {
        setName(NAME);
        setCode(CODE);
    }
}
