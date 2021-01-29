package org.name.tool.core.metrics.classlevel.cai;

import org.name.tool.core.metrics.api.ClassMetric;

public abstract class CAI extends ClassMetric<Double> {
    public static final String NAME = "Classified Attributes Interactions";
    public static final String CODE = "CAI";

    protected CAI() {
        setName(NAME);
        setCode(CODE);
    }
}