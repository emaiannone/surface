package org.name.tool.core.metrics.cm;

import org.name.tool.core.metrics.api.SecurityMetric;

public abstract class ClassifiedMethods extends SecurityMetric {
    public static final String NAME = "Classified Methods";
    public static final String CODE = "CM";

    protected ClassifiedMethods() {
        setName(NAME);
        setCode(CODE);
    }
}
