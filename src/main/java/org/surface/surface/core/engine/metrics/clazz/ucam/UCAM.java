package org.surface.surface.core.engine.metrics.clazz.ucam;

import org.surface.surface.core.engine.metrics.api.ClassMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class UCAM extends ClassMetric<DoubleMetricValue> {
    public static final String NAME = "Uncalled Classified Accessor Method";
    public static final String CODE = "UCAM";

    UCAM() {
        setName(NAME);
        setCode(CODE);
    }
}
