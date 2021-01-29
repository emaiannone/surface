package org.name.tool.core.metrics.classlevel.rp;

import org.name.tool.core.metrics.api.ClassMetric;

public abstract class RP extends ClassMetric<Boolean> {
    public static final String NAME = "Reflection Package";
    public static final String CODE = "RP";

    protected RP() {
        setName(NAME);
        setCode(CODE);
    }
}
