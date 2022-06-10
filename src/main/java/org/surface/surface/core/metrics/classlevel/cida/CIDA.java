package org.surface.surface.core.metrics.classlevel.cida;

import org.surface.surface.core.metrics.api.ClassMetric;
import org.surface.surface.core.metrics.results.values.DoubleMetricValue;

public abstract class CIDA extends ClassMetric<DoubleMetricValue> {
    public static final String NAME = "Classified Instance Data Accessibility";
    public static final String CODE = "CIDA";

    CIDA() {
        setName(NAME);
        setCode(CODE);
    }
}
