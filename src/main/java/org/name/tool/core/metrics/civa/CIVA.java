package org.name.tool.core.metrics.civa;

import org.name.tool.core.metrics.api.SecurityMetric;

public abstract class CIVA extends SecurityMetric {
    public static final String NAME = "Classified Instance Variables Accessibility";
    public static final String CODE = "CIVA";

    protected CIVA() {
        setName(NAME);
        setCode(CODE);
    }
}
