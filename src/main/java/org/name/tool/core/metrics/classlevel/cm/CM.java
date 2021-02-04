package org.name.tool.core.metrics.classlevel.cm;

import org.name.tool.core.metrics.api.ClassMetric;
import org.name.tool.results.values.IntMetricValue;

public abstract class CM extends ClassMetric<IntMetricValue> {
    public static final String NAME = "Classified Methods";
    public static final String CODE = "CM";

    protected CM() {
        setName(NAME);
        setCode(CODE);
    }
}
