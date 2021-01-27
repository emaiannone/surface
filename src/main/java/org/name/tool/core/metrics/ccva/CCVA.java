package org.name.tool.core.metrics.ccva;

import org.name.tool.core.metrics.api.SecurityMetric;

public abstract class CCVA extends SecurityMetric<Double> {
    public static final String NAME = "Classified Class Variables Accessibility";
    public static final String CODE = "CCVA";

    protected CCVA() {
        setName(NAME);
        setCode(CODE);
    }
}
