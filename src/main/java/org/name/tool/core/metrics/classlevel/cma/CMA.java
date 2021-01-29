package org.name.tool.core.metrics.classlevel.cma;

import org.name.tool.core.metrics.api.ClassMetric;

public abstract class CMA extends ClassMetric<Double> {
    public static final String NAME = "Classified Method Accessibility";
    public static final String CODE = "CMA";

    protected CMA() {
        setName(NAME);
        setCode(CODE);
    }
}
