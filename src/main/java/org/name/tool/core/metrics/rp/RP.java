package org.name.tool.core.metrics.rp;

import org.name.tool.core.metrics.api.SecurityMetric;

public abstract class RP extends SecurityMetric {
    public static final String NAME = "Reflection Package";
    public static final String CODE = "RP";

    protected RP() {
        setName(NAME);
        setCode(CODE);
    }
}
