package org.surface.surface.core.metrics.classlevel.coa;

import org.surface.surface.core.metrics.api.ClassMetric;
import org.surface.surface.core.metrics.results.values.DoubleMetricValue;

public abstract class COA extends ClassMetric<DoubleMetricValue> {
    public static final String NAME = "Classified Operation Accessibility";
    public static final String CODE = "COA";

    COA() {
        setName(NAME);
        setCode(CODE);
    }
}
