package org.surface.surface.core.engine.metrics.clazz.caai;

import org.surface.surface.core.engine.metrics.api.ClassMetric;
import org.surface.surface.core.engine.metrics.results.values.DoubleMetricValue;

public abstract class CAAI extends ClassMetric<DoubleMetricValue> {
    public static final String NAME = "Classified Accessor Attribute Interactions (Class)";
    public static final String CODE = "CAAI";

    CAAI() {
        setName(NAME);
        setCode(CODE);
    }
}
