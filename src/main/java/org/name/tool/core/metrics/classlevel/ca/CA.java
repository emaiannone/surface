package org.name.tool.core.metrics.classlevel.ca;

import org.name.tool.core.metrics.api.ClassMetric;
import org.name.tool.results.values.IntMetricValue;

public abstract class CA extends ClassMetric<IntMetricValue> {
    public static final String NAME = "Classified Attributes";
    public static final String CODE = "CA";

    protected CA() {
        setName(NAME);
        setCode(CODE);
    }
}
