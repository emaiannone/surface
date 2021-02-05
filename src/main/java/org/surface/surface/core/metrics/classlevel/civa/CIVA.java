package org.surface.surface.core.metrics.classlevel.civa;

import org.surface.surface.core.metrics.api.ClassMetric;
import org.surface.surface.results.values.DoubleMetricValue;

public abstract class CIVA extends ClassMetric<DoubleMetricValue> {
    public static final String NAME = "Classified Instance Variables Accessibility";
    public static final String CODE = "CIVA";

    protected CIVA() {
        setName(NAME);
        setCode(CODE);
    }
}
