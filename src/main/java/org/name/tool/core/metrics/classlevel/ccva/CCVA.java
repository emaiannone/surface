package org.name.tool.core.metrics.classlevel.ccva;

import org.name.tool.core.metrics.api.ClassMetric;
import org.name.tool.results.values.DoubleMetricValue;

public abstract class CCVA extends ClassMetric<DoubleMetricValue> {
    public static final String NAME = "Classified Class Variables Accessibility";
    public static final String CODE = "CCVA";

    protected CCVA() {
        setName(NAME);
        setCode(CODE);
    }
}
