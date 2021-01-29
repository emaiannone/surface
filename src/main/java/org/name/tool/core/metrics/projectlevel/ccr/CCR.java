package org.name.tool.core.metrics.projectlevel.ccr;

import org.name.tool.core.metrics.api.ProjectSecurityMetric;

public abstract class CCR extends ProjectSecurityMetric<Double> {
    public static final String NAME = "Critical Classes Ratio";
    public static final String CODE = "CCR";

    protected CCR() {
        setName(NAME);
        setCode(CODE);
    }
}
