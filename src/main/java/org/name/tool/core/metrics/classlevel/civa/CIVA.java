package org.name.tool.core.metrics.classlevel.civa;

import org.name.tool.core.metrics.api.ClassMetric;
import org.name.tool.results.values.DoubleMetricValue;

public abstract class CIVA extends ClassMetric<DoubleMetricValue> {
    public static final String NAME = "Classified Instance Variables Accessibility";
    public static final String CODE = "CIVA";

    protected CIVA() {
        setName(NAME);
        setCode(CODE);
    }
}
