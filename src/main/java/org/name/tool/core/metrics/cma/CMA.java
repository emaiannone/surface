package org.name.tool.core.metrics.cma;

import org.name.tool.core.metrics.api.SecurityMetric;

public abstract class CMA extends SecurityMetric<Double> {
    public static final String NAME = "Classified Method Accessibility";
    public static final String CODE = "CMA";

    protected CMA() {
        setName(NAME);
        setCode(CODE);
    }
}
