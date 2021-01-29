package org.name.tool.core.metrics.projectlevel.cscr;

import org.name.tool.core.metrics.api.ProjectSecurityMetric;

import java.util.Map;

public abstract class CSCR extends ProjectSecurityMetric<Map<String, Double>> {
    public static final String NAME = "Critical SuperClasses Ratios";
    public static final String CODE = "CSCR";

    protected CSCR() {
        setName(NAME);
        setCode(CODE);
    }
}
