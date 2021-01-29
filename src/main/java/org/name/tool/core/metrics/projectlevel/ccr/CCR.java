package org.name.tool.core.metrics.projectlevel.ccr;

import org.name.tool.core.metrics.api.ProjectMetric;

public abstract class CCR extends ProjectMetric<Double> {
    public static final String NAME = "Critical Classes Ratio";
    public static final String CODE = "CCR";

    protected CCR() {
        setName(NAME);
        setCode(CODE);
    }
}
