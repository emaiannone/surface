package org.name.tool.core.metrics.classlevel.ccva;

import org.name.tool.core.metrics.api.ClassMetric;

public abstract class CCVA extends ClassMetric<Double> {
    public static final String NAME = "Classified Class Variables Accessibility";
    public static final String CODE = "CCVA";

    protected CCVA() {
        setName(NAME);
        setCode(CODE);
    }
}
