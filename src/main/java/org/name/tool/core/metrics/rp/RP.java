package org.name.tool.core.metrics.rp;

import org.name.tool.core.metrics.api.ClassSecurityMetric;

public abstract class RP extends ClassSecurityMetric<Boolean> {
    public static final String NAME = "Reflection Package";
    public static final String CODE = "RP";

    protected RP() {
        setName(NAME);
        setCode(CODE);
    }
}
