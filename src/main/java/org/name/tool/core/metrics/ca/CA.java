package org.name.tool.core.metrics.ca;

import org.name.tool.core.metrics.api.ClassSecurityMetric;

public abstract class CA extends ClassSecurityMetric<Integer> {
    public static final String NAME = "Classified Attributes";
    public static final String CODE = "CA";

    protected CA() {
        setName(NAME);
        setCode(CODE);
    }
}
