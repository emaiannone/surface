package org.name.tool.core.metrics.cm;

import org.name.tool.core.metrics.api.SecurityMetric;

public abstract class CM extends SecurityMetric {
    public static final String NAME = "Classified Methods";
    public static final String CODE = "CM";

    protected CM() {
        setName(NAME);
        setCode(CODE);
    }
}
