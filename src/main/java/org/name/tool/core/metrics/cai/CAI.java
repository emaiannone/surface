package org.name.tool.core.metrics.cai;

import org.name.tool.core.metrics.api.SecurityMetric;

public abstract class CAI extends SecurityMetric<Double> {
    public static final String NAME = "Classified Attributes Interactions";
    public static final String CODE = "CAI";

    protected CAI() {
        setName(NAME);
        setCode(CODE);
    }
}