package org.name.tool.core.metrics.ccva;

import org.name.tool.core.metrics.api.ClassSecurityMetric;

public abstract class CCVA extends ClassSecurityMetric<Double> {
    public static final String NAME = "Classified Class Variables Accessibility";
    public static final String CODE = "CCVA";

    protected CCVA() {
        setName(NAME);
        setCode(CODE);
    }
}
