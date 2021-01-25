package org.name.tool.core.metrics.civa;

import org.name.tool.core.metrics.api.SecurityMetric;

public abstract class ClassifiedInstanceVariablesAccessibility extends SecurityMetric {
    public static final String NAME = "Classified Instance Variables Accessibility";
    public static final String CODE = "CIVA";

    protected ClassifiedInstanceVariablesAccessibility() {
        setName(NAME);
        setCode(CODE);
    }
}
