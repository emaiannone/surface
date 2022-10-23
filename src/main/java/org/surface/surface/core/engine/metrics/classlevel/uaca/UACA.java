package org.surface.surface.core.engine.metrics.classlevel.uaca;

import org.surface.surface.core.engine.metrics.api.ClassMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class UACA extends ClassMetric<DoubleMetricValue> {
    public static final String NAME = "Unaccessed Assigned Classified Attribute";
    public static final String CODE = "UACA";

    UACA() {
        setName(NAME);
        setCode(CODE);
    }
}
