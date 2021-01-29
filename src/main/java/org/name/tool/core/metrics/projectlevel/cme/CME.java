package org.name.tool.core.metrics.projectlevel.cme;

import org.name.tool.core.metrics.api.ProjectSecurityMetric;

public abstract class CME extends ProjectSecurityMetric<Double> {
    public static final String NAME = "Critical Methods Extensibility";
    public static final String CODE = "CME";

    protected CME() {
        setName(NAME);
        setCode(CODE);
    }
}
