package org.name.tool.core.metrics.projectlevel.cce;

import org.name.tool.core.metrics.api.ProjectSecurityMetric;

public abstract class CCE extends ProjectSecurityMetric<Double> {
    public static final String NAME = "Critical Classes Extensibility";
    public static final String CODE = "CCE";

    protected CCE() {
        setName(NAME);
        setCode(CODE);
    }
}
