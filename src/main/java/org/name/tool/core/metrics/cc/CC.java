package org.name.tool.core.metrics.cc;

import org.name.tool.core.metrics.api.ProjectSecurityMetric;

public abstract class CC extends ProjectSecurityMetric<Integer> {
    public static final String NAME = "Critical Classes";
    public static final String CODE = "CC";

    protected CC() {
        setName(NAME);
        setCode(CODE);
    }
}
