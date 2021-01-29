package org.name.tool.core.metrics.classlevel.cmr;

import org.name.tool.core.metrics.api.ClassMetric;

public abstract class CMR extends ClassMetric<Double> {
    public static final String NAME = "Classified Methods Ratio";
    public static final String CODE = "CMR";

    protected CMR() {
        setName(NAME);
        setCode(CODE);
    }
}
