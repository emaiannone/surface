package org.name.tool.core.metrics.ca;

import org.name.tool.core.metrics.api.SecurityMetric;

public abstract class ClassifiedAttributes extends SecurityMetric {
    public static final String NAME = "Classified Attributes";
    public static final String CODE = "CA";

    protected ClassifiedAttributes() {
        setName(NAME);
        setCode(CODE);
    }
}
