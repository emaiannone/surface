package org.surface.surface.core.metrics.classlevel.ccva;

import org.surface.surface.core.metrics.api.ClassMetric;
import org.surface.surface.core.metrics.results.values.DoubleMetricValue;

public abstract class CCVA extends ClassMetric<DoubleMetricValue> {
    private static final String NAME = "Classified Class Variables Accessibility";
    public static final String CODE = "CCVA";

    CCVA() {
        setName(NAME);
        setCode(CODE);
    }
}
