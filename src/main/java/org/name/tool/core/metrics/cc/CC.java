package org.name.tool.core.metrics.cc;

import org.name.tool.core.metrics.api.SecurityMetric;

public abstract class CC extends SecurityMetric<Boolean> {
    public static final String NAME = "Critical Classes";
    public static final String CODE = "CC";

    protected CC() {
        setName(NAME);
        setCode(CODE);
    }
}
