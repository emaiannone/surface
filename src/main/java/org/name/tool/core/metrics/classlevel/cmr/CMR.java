package org.name.tool.core.metrics.classlevel.cmr;

import org.name.tool.core.metrics.api.ClassMetric;
import org.name.tool.results.values.DoubleMetricValue;

public abstract class CMR extends ClassMetric<DoubleMetricValue> {
    public static final String NAME = "Classified Methods Ratio";
    public static final String CODE = "CMR";

    protected CMR() {
        setName(NAME);
        setCode(CODE);
    }
}
