package org.name.tool.core.metrics.cmr;

import org.name.tool.core.metrics.api.SecurityMetric;

public abstract class CMR extends SecurityMetric {
    public static final String NAME = "Classified Methods Ratio";
    public static final String CODE = "CMR";

    protected CMR() {
        setName(NAME);
        setCode(CODE);
    }
}
