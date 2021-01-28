package org.name.tool.core.metrics.cai;

import org.name.tool.core.metrics.api.ClassSecurityMetric;

public abstract class CAI extends ClassSecurityMetric<Double> {
    public static final String NAME = "Classified Attributes Interactions";
    public static final String CODE = "CAI";

    protected CAI() {
        setName(NAME);
        setCode(CODE);
    }
}