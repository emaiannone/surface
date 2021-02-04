package org.name.tool.core.metrics.projectlevel.sccr;

import org.name.tool.core.metrics.api.ProjectMetric;
import org.name.tool.results.values.DoubleMetricValue;

public abstract class SCCR extends ProjectMetric<DoubleMetricValue> {
    public static final String NAME = "Serializable Critical Classes Ratio";
    public static final String CODE = "SCCR";

    protected SCCR() {
        setName(NAME);
        setCode(CODE);
    }
}
