package org.name.tool.core.metrics.ccva;

import org.name.tool.core.metrics.api.SecurityMetric;

public abstract class ClassifiedClassVariablesAccessibility extends SecurityMetric {
    public static final String NAME = "Classified Class Variables Accessibility";
    public static final String CODE = "CCVA";

    protected ClassifiedClassVariablesAccessibility() {
        setName(NAME);
        setCode(CODE);
    }
}
