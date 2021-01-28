package org.name.tool.core.metrics.civa;

import org.name.tool.core.metrics.api.ClassSecurityMetric;

public abstract class CIVA extends ClassSecurityMetric<Double> {
    public static final String NAME = "Classified Instance Variables Accessibility";
    public static final String CODE = "CIVA";

    protected CIVA() {
        setName(NAME);
        setCode(CODE);
    }
}
